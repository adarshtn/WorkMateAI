package com.example.workmateai.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
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
import com.example.workmateai.Job
import com.example.workmateai.ui.theme.WorkMateAITheme

class JobResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jobs = intent.getParcelableArrayListExtra<Job>("JOB_LIST") ?: emptyList()
        val enhancedSkills = intent.getStringArrayListExtra("ENHANCED_SKILLS") ?: emptyList()
        Log.d("JobResultsActivity", "Activity started with jobs: $jobs, enhanced skills: $enhancedSkills")
        setContent {
            WorkMateAITheme {
                JobResultsScreen(jobs, enhancedSkills)
            }
        }
    }
}

@Composable
fun JobResultsScreen(jobs: List<Job>, enhancedSkills: List<String>) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(jobs) { isLoading = false }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Job Listings",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (enhancedSkills.isNotEmpty()) {
            Text(
                "AI-Enhanced Skills: ${enhancedSkills.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading jobs...", style = MaterialTheme.typography.bodyMedium)
            }
        } else if (jobs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No job listings available.", style = MaterialTheme.typography.bodyMedium)
                Log.d("JobResultsActivity", "No jobs to display")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(jobs) { job ->
                    JobCard(job) {
                        Log.d("JobResultsActivity", "Job clicked: ${job.link}")
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.link))
                        context.startActivity(intent)
                    }
                }
            }
            Log.d("JobResultsActivity", "Jobs displayed: ${jobs.size}")
        }
    }
}

@Composable
fun JobCard(job: Job, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(job.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text(job.company, style = MaterialTheme.typography.bodyMedium)
            Text("Location: ${job.location}", style = MaterialTheme.typography.bodySmall)
        }
    }
}