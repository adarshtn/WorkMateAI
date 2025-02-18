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
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.MaterialTheme


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

    // Column for arranging elements vertically
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Adds padding around the whole layout
        horizontalAlignment = Alignment.CenterHorizontally, // Centers elements horizontally
        verticalArrangement = Arrangement.Center // Centers elements vertically
    ) {
        // Heading "WorkMate AI"
        Text(
            text = "WorkMate AI",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black // Set heading color to black
            ),
            modifier = Modifier.padding(bottom = 32.dp) // Adds padding below the heading
        )

        // Modifier for the buttons to make them fill the width and add padding
        val buttonModifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

        // Button for creating a new resume
        Button(
            onClick = {
                // Navigate to the CreateResumeActivity
                val intent = Intent(context, CreateResumeActivity::class.java)
                context.startActivity(intent)
            },
            modifier = buttonModifier
        ) {
            Text("Create Resume")
        }


        // Button for viewing saved resume
        Button(
            onClick = {
                // Handle button click for viewing saved resume
            },
            modifier = buttonModifier
        ) {
            Text("View Saved Resume")
        }


        // Button for job suggestions
        Button(
            onClick = {
                // Handle button click for job suggestions
            },
            modifier = buttonModifier
        ) {
            Text("Job Suggestions")
        }
    }
}
