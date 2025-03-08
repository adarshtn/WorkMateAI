package com.example.workmateai

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.workmateai.ui.JobResultsActivity
import com.example.workmateai.ui.theme.WorkMateAITheme
import com.example.workmateai.utils.JobSearchUtils
import com.example.workmateai.utils.PDFUtils
import com.example.workmateai.utils.SkillEnhancer
import kotlinx.coroutines.*
import java.io.File

data class Job(
    val title: String,
    val company: String,
    val location: String,
    val link: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(company)
        parcel.writeString(location)
        parcel.writeString(link)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Job> {
        override fun createFromParcel(parcel: Parcel): Job = Job(parcel)
        override fun newArray(size: Int): Array<Job?> = arrayOfNulls(size)
    }
}

class JobSuggestionsActivity : ComponentActivity() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestStoragePermissions()

        setContent {
            WorkMateAITheme {
                JobSuggestionsScreen()
            }
        }
    }

    private fun requestStoragePermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 100
    }

    @Composable
    fun JobSuggestionsScreen() {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val resumeDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        var resumeList by remember { mutableStateOf(resumeDir?.listFiles()?.toList() ?: listOf()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Select Resume for Job Suggestions",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (resumeList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(resumeList) { file ->
                        ResumeSelectionCard(file) { selectedFile ->
                            coroutineScope.launch {
                                processResumeFile(context, selectedFile)
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No saved resumes found.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }

    @Composable
    fun ResumeSelectionCard(file: File, onSelect: (File) -> Unit) {
        var showDialog by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = file.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        "Click 'Select' to fetch suggestions",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(onClick = { showDialog = true }) {
                    Text("Select")
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirm Selection") },
                text = { Text("Do you want to fetch job suggestions for ${file.name}?") },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { showDialog = false }) {
                            Text("No")
                        }

                        Button(onClick = {
                            showDialog = false
                            onSelect(file)
                        }) {
                            Text("Yes")
                        }
                    }
                }
            )
        }
    }

    private var currentToast: Toast? = null

    private fun showToastOnce(context: Context, message: String) {
        val appContext = context.applicationContext
        currentToast?.cancel()
        currentToast = Toast.makeText(appContext, message, Toast.LENGTH_SHORT).apply {
            show()
        }
    }

    suspend fun processResumeFile(context: Context, selectedFile: File) {
        try {
            val extractedText = withContext(Dispatchers.IO) {
                PDFUtils.extractTextFromPDF(context, selectedFile)
            }

            if (extractedText.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    showToastOnce(context, "Unable to extract text from PDF")
                }
                return
            }

            val skillsList: List<String> = withContext(Dispatchers.IO) {
                PDFUtils.extractSkillsFromPDFText(extractedText)
            }

            withContext(Dispatchers.Main) {
                if (skillsList.isNotEmpty()) {
                    showToastOnce(context, "Skills extracted: ${skillsList.joinToString(", ")}")
                } else {
                    showToastOnce(context, "No skills found in Skills section")
                }
            }

            val enhancedSkills = withContext(Dispatchers.IO) {
                SkillEnhancer.enhanceSkills(skillsList)
            }

            val jobs = withContext(Dispatchers.IO) {
                // Use up to 4 original skills only
                val searchSkills = skillsList.take(4).distinctBy { it.lowercase() }
                android.util.Log.d("JobSuggestions", "Search skills: $searchSkills")
                JobSearchUtils.searchJobs(context, searchSkills)
                    .map { adzunaJob ->
                        Job(
                            title = adzunaJob.title,
                            company = adzunaJob.company,
                            location = adzunaJob.location,
                            link = adzunaJob.redirectUrl
                        )
                    }
            }

            withContext(Dispatchers.Main) {
                android.util.Log.d("JobSuggestions", "Enhanced skills (for display): $enhancedSkills")
                android.util.Log.d("JobSuggestions", "Jobs before intent: $jobs")
                val intent = Intent(context, JobResultsActivity::class.java).apply {
                    putParcelableArrayListExtra("JOB_LIST", ArrayList(jobs))
                    putStringArrayListExtra("ENHANCED_SKILLS", ArrayList(enhancedSkills))
                }
                android.util.Log.d("JobSuggestions", "Launching JobResultsActivity with intent")
                context.startActivity(intent)
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                showToastOnce(context, "Processing Error: ${e.localizedMessage}")
            }
        }
    }
}
