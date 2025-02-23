package com.example.workmateai

import com.example.workmateai.CreateResumeActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import com.example.workmateai.ui.theme.WorkMateAITheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.MaterialTheme
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkMateAITheme {
                HomeScreen()
            }
        }
    }
}
@Composable
fun HomeScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Name
        Text(
            text = "WorkMate AI",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 24.dp) // Reduced bottom padding
        )

        // Standard button modifier
        val buttonModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp) // Reduced vertical padding for compact layout

        // Buttons
        Button(
            onClick = { context.startActivity(Intent(context, CreateResumeActivity::class.java)) },
            modifier = buttonModifier
        ) {
            Text("Create Resume")
        }

        Button(
            onClick = { context.startActivity(Intent(context, ViewResumesActivity::class.java)) },
            modifier = buttonModifier
        ) {
            Text("View Saved Resumes")
        }

        Button(
            onClick = {  context.startActivity(Intent(context, JobSuggestionsActivity::class.java)) },
            modifier = buttonModifier
        ) {
            Text("Job Suggestions")
        }
    }
}
