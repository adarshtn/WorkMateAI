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
import androidx.compose.material3.MaterialTheme

class ViewResumesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkMateAITheme {
                ViewResumesScreen()
            }
        }
    }
}

@Composable
fun ViewResumesScreen() {
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
            "Saved Resumes",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // List of resumes in a styled card layout
        if (resumeList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(resumeList) { file ->
                    ResumeCard(
                        file = file,
                        onView = {
                            openPDF(context, file)
                        },
                        onDelete = {
                            file.delete()
                            resumeList = resumeDir?.listFiles()?.toList() ?: listOf()
                            Toast.makeText(context, "Deleted ${file.name}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Centers text in the middle of the screen
            ) {
                Text("No saved resumes found.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
@Composable
fun ResumeCard(file: File, onView: () -> Unit, onDelete: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) } // State to control the dialog visibility

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onView() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(6.dp)
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
                    text = "Tap to view",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = { showDialog = true }, // Show confirmation dialog
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Delete")
            }
        }
    }

    // Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete ${file.name}?") },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center // Center-align buttons
                ) {
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            showDialog = false
                            onDelete()
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),

                    ) {
                        Text("Delete")
                    }


                }
            }
        )
    }
}
