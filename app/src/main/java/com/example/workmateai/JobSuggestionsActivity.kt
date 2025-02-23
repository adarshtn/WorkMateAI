package com.example.workmateai

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.workmateai.ui.theme.WorkMateAITheme
import java.io.File

class JobSuggestionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkMateAITheme {
                JobSuggestionsScreen()
            }
        }
    }
}

@Composable
fun JobSuggestionsScreen() {
    val context = LocalContext.current
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
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (resumeList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(resumeList) { file ->
                    ResumeSelectionCard(
                        file = file,
                        onSelect = {
                            Toast.makeText(context, "Fetching suggestions for ${file.name}", Toast.LENGTH_SHORT).show()
                            // Add logic to fetch job suggestions here
                        }
                    )
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
fun ResumeSelectionCard(file: File, onSelect: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) } // Control dialog visibility

    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
            // File Name and Hint Text
            Column {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Click 'Select' to fetch suggestions",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Only the Select button triggers the dialog
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("Select")
            }
        }
    }

    // Confirmation Dialog
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
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text("No")
                    }

                    Button(
                        onClick = {
                            showDialog = false
                            onSelect() // Trigger job suggestions
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Yes")
                    }
                }
            }
        )
    }
}
