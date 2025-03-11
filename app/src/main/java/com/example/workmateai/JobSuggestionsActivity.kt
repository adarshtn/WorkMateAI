package com.example.workmateai

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.workmateai.ui.JobResultsActivity
import com.example.workmateai.ui.theme.WorkMateAITheme
import com.example.workmateai.utils.JobSearchUtils
import com.example.workmateai.utils.PDFUtils
import com.example.workmateai.utils.SkillEnhancer
import kotlinx.coroutines.*
import java.io.File
import kotlin.math.PI
import kotlin.math.sin

// Data class Job (unchanged)
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

        // Animation setup from WelcomeActivity
        val infiniteTransition = rememberInfiniteTransition()
        val position by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 8000,
                    easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
        val secondaryPosition by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 12000,
                    easing = CubicBezierEasing(0.2f, 0.0f, 0.8f, 1.0f)
                ),
                repeatMode = RepeatMode.Reverse
            )
        )

        // Gradient colors from WelcomeActivity (background unchanged)
        val gradientColors = listOf(
            Color(0xFF000000), // Pure Black
            Color(0xFF434343)  // Near-black with hint of blue
        )
        // Original button gradient with vertical orientation
        val buttonGradient = listOf(
            Color(0xFF5B5959), // Midnight navy (top)
            Color(0xFF252424)  // Deep navy (bottom)
        )

        // Wave offset for background animation
        val waveOffset = sin(position * 2 * PI).toFloat() * 0.5f + 0.5f
        val secondWave = sin(secondaryPosition * 2 * PI).toFloat() * 0.5f + 0.5f
        val gradientSize = 3000f

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = androidx.compose.ui.geometry.Offset(
                            x = gradientSize * waveOffset,
                            y = gradientSize * 0.3f * secondWave
                        ),
                        end = androidx.compose.ui.geometry.Offset(
                            x = gradientSize * (1f - waveOffset) * 0.8f,
                            y = gradientSize * (0.7f + 0.3f * secondWave)
                        )
                    )
                )
        ) {
            // Subtle overlay for depth
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x00000000),
                                Color(0x08071428)
                            ),
                            center = androidx.compose.ui.geometry.Offset(
                                x = secondWave * 1000f,
                                y = waveOffset * 1000f
                            ),
                            radius = 1200f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Adjusted heading: smaller font size for one line
                Text(
                    text = "Select Resume for Job Suggestions",
                    fontSize = 20.sp, // Reduced to 20.sp to ensure one line
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (resumeList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(resumeList) { file ->
                            ResumeSelectionCard(
                                file = file,
                                onSelect = { selectedFile ->
                                    coroutineScope.launch {
                                        processResumeFile(context, selectedFile)
                                    }
                                },
                                buttonGradient = buttonGradient
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No saved resumes found.",
                            fontSize = 18.sp,
                            color = Color(0xFFFFFFFF)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ResumeSelectionCard(file: File, onSelect: (File) -> Unit, buttonGradient: List<Color>) {
        var showDialog by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252424)), // Dark navy
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
                Column(
                    modifier = Modifier.weight(1f) // Ensures text takes available space
                ) {
                    Text(
                        text = file.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFFFFF)
                    )
                }

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .height(48.dp)
                        .width(120.dp), // Fixed width to match screenshot
                    shape = RoundedCornerShape(24.dp), // Pill shape matching screenshot
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    contentPadding = PaddingValues(0.dp) // Remove default padding
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = buttonGradient
                                ),
                                shape = RoundedCornerShape(24.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Select",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFFFFFFF)
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirm Selection", color = Color(0xFFFFFFFF)) },
                text = { Text("Do you want to fetch job suggestions for ${file.name}?", color = Color(0xFFCCCCCC)) },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .height(48.dp)
                                .width(120.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = buttonGradient
                                        ),
                                        shape = RoundedCornerShape(24.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFFFFFFF)
                                )
                            }
                        }

                        Button(
                            onClick = {
                                showDialog = false
                                onSelect(file)
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .width(120.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = buttonGradient
                                        ),
                                        shape = RoundedCornerShape(24.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Yes",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFFFFFFF)
                                )
                            }
                        }
                    }
                },
                containerColor = Color(0xFF252424) // Dark navy background for dialog
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