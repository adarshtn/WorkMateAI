package com.example.workmateai

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import com.example.workmateai.ui.theme.WorkMateAITheme

class CreateResumeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkMateAITheme {
                CreateResumeScreen()
            }
        }
    }
}

@Composable
fun CreateResumeScreen() {
    val context = LocalContext.current

    // State for each field
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var educationFields by remember { mutableStateOf(listOf("")) }
    var skillsFields by remember { mutableStateOf(listOf("")) }
    var experienceFields by remember { mutableStateOf(listOf("")) }
    var projectsFields by remember { mutableStateOf(listOf("")) }

    // Scrollable Column to allow for scrolling when fields are added
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Make the column scrollable
            .padding(16.dp), // Add padding around the content
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp) // Space between elements
    ) {
        Text(
            text = "Create Resume",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Input fields for Name, Phone, Location
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        // Education Fields with "Add" button to add more
        Text("Education", fontWeight = FontWeight.Bold)
        educationFields.forEachIndexed { index, field ->
            OutlinedTextField(
                value = field,
                onValueChange = {
                    val updatedFields = educationFields.toMutableList().apply { this[index] = it }
                    educationFields = updatedFields
                },
                label = { Text("Education $index") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(onClick = { educationFields = educationFields + "" }) {
            Text("Add Education")
        }

        // Skills Fields with "Add" button to add more
        Text("Skills", fontWeight = FontWeight.Bold)
        skillsFields.forEachIndexed { index, field ->
            OutlinedTextField(
                value = field,
                onValueChange = {
                    val updatedFields = skillsFields.toMutableList().apply { this[index] = it }
                    skillsFields = updatedFields
                },
                label = { Text("Skill $index") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(onClick = { skillsFields = skillsFields + "" }) {
            Text("Add Skill")
        }

        // Experience Fields with "Add" button to add more
        Text("Experience", fontWeight = FontWeight.Bold)
        experienceFields.forEachIndexed { index, field ->
            OutlinedTextField(
                value = field,
                onValueChange = {
                    val updatedFields = experienceFields.toMutableList().apply { this[index] = it }
                    experienceFields = updatedFields
                },
                label = { Text("Experience $index") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(onClick = { experienceFields = experienceFields + "" }) {
            Text("Add Experience")
        }

        // Projects Fields with "Add" button to add more
        Text("Projects", fontWeight = FontWeight.Bold)
        projectsFields.forEachIndexed { index, field ->
            OutlinedTextField(
                value = field,
                onValueChange = {
                    val updatedFields = projectsFields.toMutableList().apply { this[index] = it }
                    projectsFields = updatedFields
                },
                label = { Text("Project $index") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(onClick = { projectsFields = projectsFields + "" }) {
            Text("Add Project")
        }

        // Submit Button
        Button(
            onClick = {
                // Your logic to handle resume creation
                Toast.makeText(context, "Resume Created", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Resume")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateResumePreview() {
    WorkMateAITheme {
        CreateResumeScreen()
    }
}
