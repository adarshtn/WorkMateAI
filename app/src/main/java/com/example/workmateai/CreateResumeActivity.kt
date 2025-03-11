package com.example.workmateai

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.workmateai.ui.theme.WorkMateAITheme
import kotlin.math.PI
import kotlin.math.sin

// Data classes (unchanged)
data class Education(
    var course: String = "",
    var institution: String = "",
    var yearOfPassing: String = "",
    var percentage: String = ""
)

data class Skill(
    var category: String = "",
    var items: String = ""
)

data class Project(
    var title: String = "",
    var technologies: String = "",
    var description: MutableList<String> = mutableStateListOf("")
)

data class Certificate(
    var name: String = ""
)

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

    // State for each field (unchanged)
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }
    var objective by remember { mutableStateOf("") }

    var educationList by remember { mutableStateOf(listOf(Education())) }
    var skillsList by remember { mutableStateOf(listOf(
        Skill("Programming Languages", ""),
        Skill("Libraries/Framework", ""),
        Skill("Tools/Platforms", ""),
        Skill("Databases", "")
    )) }
    var projectsList by remember { mutableStateOf(listOf(Project())) }
    var certificatesList by remember { mutableStateOf(listOf(Certificate())) }

    val scrollState = rememberScrollState()

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

    // Gradient colors from WelcomeActivity
    val gradientColors = listOf(
        ComposeColor(0xFF000000), // Pure Black
        ComposeColor(0xFF434343)  // Near-black with hint of blue
    )
    val buttonGradient = listOf(
        ComposeColor(0xFF5B5959), // Midnight navy
        ComposeColor(0xFF252424)  // Deep navy
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
                            ComposeColor(0x00000000),
                            ComposeColor(0x08071428)
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
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header styled like WelcomeActivity
            Text(
                text = "Create Resume",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ComposeColor(0xFFFFFFFF),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Personal Information Section
            SectionHeader("Personal Information")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name", color = ComposeColor(0xFFFFFFFF)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ComposeColor(0xFFFFFFFF),
                    unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                    focusedTextColor = ComposeColor(0xFFFFFFFF),
                    unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                    cursorColor = ComposeColor(0xFFFFFFFF)
                )
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = ComposeColor(0xFFFFFFFF)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ComposeColor(0xFFFFFFFF),
                    unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                    focusedTextColor = ComposeColor(0xFFFFFFFF),
                    unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                    cursorColor = ComposeColor(0xFFFFFFFF)
                )
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number", color = ComposeColor(0xFFFFFFFF)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ComposeColor(0xFFFFFFFF),
                    unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                    focusedTextColor = ComposeColor(0xFFFFFFFF),
                    unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                    cursorColor = ComposeColor(0xFFFFFFFF)
                )
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location", color = ComposeColor(0xFFFFFFFF)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ComposeColor(0xFFFFFFFF),
                    unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                    focusedTextColor = ComposeColor(0xFFFFFFFF),
                    unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                    cursorColor = ComposeColor(0xFFFFFFFF)
                )
            )
            OutlinedTextField(
                value = linkedin,
                onValueChange = { linkedin = it },
                label = { Text("LinkedIn Profile", color = ComposeColor(0xFFFFFFFF)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ComposeColor(0xFFFFFFFF),
                    unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                    focusedTextColor = ComposeColor(0xFFFFFFFF),
                    unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                    cursorColor = ComposeColor(0xFFFFFFFF)
                )
            )

            // Objective Section
            SectionHeader("Objective")
            OutlinedTextField(
                value = objective,
                onValueChange = { objective = it },
                label = { Text("Professional Objective", color = ComposeColor(0xFFFFFFFF)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ComposeColor(0xFFFFFFFF),
                    unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                    focusedTextColor = ComposeColor(0xFFFFFFFF),
                    unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                    cursorColor = ComposeColor(0xFFFFFFFF)
                )
            )

            // Education Section
            SectionHeader("Education")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                educationList.forEachIndexed { index, education ->
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = education.course,
                            onValueChange = {
                                val updatedList = educationList.toMutableList()
                                updatedList[index] = education.copy(course = it)
                                educationList = updatedList
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Course", color = ComposeColor(0xFFFFFFFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                focusedTextColor = ComposeColor(0xFFFFFFFF),
                                unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                cursorColor = ComposeColor(0xFFFFFFFF)
                            )
                        )
                        OutlinedTextField(
                            value = education.institution,
                            onValueChange = {
                                val updatedList = educationList.toMutableList()
                                updatedList[index] = education.copy(institution = it)
                                educationList = updatedList
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Institution", color = ComposeColor(0xFFFFFFFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                focusedTextColor = ComposeColor(0xFFFFFFFF),
                                unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                cursorColor = ComposeColor(0xFFFFFFFF)
                            )
                        )
                        OutlinedTextField(
                            value = education.yearOfPassing,
                            onValueChange = {
                                val updatedList = educationList.toMutableList()
                                updatedList[index] = education.copy(yearOfPassing = it)
                                educationList = updatedList
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Year", color = ComposeColor(0xFFFFFFFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                focusedTextColor = ComposeColor(0xFFFFFFFF),
                                unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                cursorColor = ComposeColor(0xFFFFFFFF)
                            )
                        )
                        OutlinedTextField(
                            value = education.percentage,
                            onValueChange = {
                                val updatedList = educationList.toMutableList()
                                updatedList[index] = education.copy(percentage = it)
                                educationList = updatedList
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("%", color = ComposeColor(0xFFFFFFFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                focusedTextColor = ComposeColor(0xFFFFFFFF),
                                unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                cursorColor = ComposeColor(0xFFFFFFFF)
                            )
                        )
                        if (educationList.size > 1) {
                            IconButton(
                                onClick = {
                                    educationList = educationList.filterIndexed { i, _ -> i != index }
                                },
                                modifier = Modifier.align(Alignment.End).size(24.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ComposeColor(0xFFFFFFFF))
                            }
                        }
                    }
                }
                Button(
                    onClick = { educationList = educationList + Education() },
                    modifier = Modifier
                        .align(Alignment.End)
                        .background(
                            brush = Brush.horizontalGradient(colors = buttonGradient),
                            shape = ButtonDefaults.shape
                        )
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Transparent),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = ComposeColor(0xFFFFFFFF))
                    Text(
                        text = "Add Education",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = ComposeColor(0xFFFFFFFF)
                    )
                }
            }

            // Skills Section
            SectionHeader("Skills")
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                skillsList.forEachIndexed { index, skill ->
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = skill.category,
                            onValueChange = {
                                val updatedList = skillsList.toMutableList()
                                updatedList[index] = skill.copy(category = it)
                                skillsList = updatedList
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Category", color = ComposeColor(0xFFFFFFFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                focusedTextColor = ComposeColor(0xFFFFFFFF),
                                unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                cursorColor = ComposeColor(0xFFFFFFFF)
                            )
                        )
                        OutlinedTextField(
                            value = skill.items,
                            onValueChange = {
                                val updatedList = skillsList.toMutableList()
                                updatedList[index] = skill.copy(items = it)
                                skillsList = updatedList
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Skills (comma separated)", color = ComposeColor(0xFFFFFFFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                focusedTextColor = ComposeColor(0xFFFFFFFF),
                                unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                cursorColor = ComposeColor(0xFFFFFFFF)
                            )
                        )
                        IconButton(
                            onClick = {
                                skillsList = skillsList.filterIndexed { i, _ -> i != index }
                            },
                            modifier = Modifier.align(Alignment.End).size(24.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ComposeColor(0xFFFFFFFF))
                        }
                    }
                }
                Button(
                    onClick = { skillsList = skillsList + Skill() },
                    modifier = Modifier
                        .align(Alignment.End)
                        .background(
                            brush = Brush.horizontalGradient(colors = buttonGradient),
                            shape = ButtonDefaults.shape
                        )
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Transparent),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = ComposeColor(0xFFFFFFFF))
                    Text(
                        text = "Add Skill Category",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = ComposeColor(0xFFFFFFFF)
                    )
                }
            }

            // Projects Section
            SectionHeader("Projects")
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                projectsList.forEachIndexed { projIndex, project ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ComposeColor(0xFFCCCCCC))
                            .padding(8.dp)
                    ) {
                        OutlinedTextField(
                            value = project.title,
                            onValueChange = {
                                val updatedList = projectsList.toMutableList()
                                updatedList[projIndex] = project.copy(title = it)
                                projectsList = updatedList
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Project Title", color = ComposeColor(0xFFFFFFFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                focusedTextColor = ComposeColor(0xFFFFFFFF),
                                unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                cursorColor = ComposeColor(0xFFFFFFFF)
                            )
                        )
                        OutlinedTextField(
                            value = project.technologies,
                            onValueChange = {
                                val updatedList = projectsList.toMutableList()
                                updatedList[projIndex] = project.copy(technologies = it)
                                projectsList = updatedList
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Technologies Used (comma separated)", color = ComposeColor(0xFFFFFFFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                focusedTextColor = ComposeColor(0xFFFFFFFF),
                                unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                cursorColor = ComposeColor(0xFFFFFFFF)
                            )
                        )
                        Text(
                            text = "Project Details:",
                            modifier = Modifier.padding(top = 8.dp),
                            fontWeight = FontWeight.Bold,
                            color = ComposeColor(0xFFFFFFFF),
                            fontSize = 16.sp
                        )
                        project.description.forEachIndexed { descIndex, desc ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = desc,
                                    onValueChange = {
                                        val updatedProject = projectsList[projIndex]
                                        updatedProject.description[descIndex] = it
                                        val updatedList = projectsList.toMutableList()
                                        projectsList = updatedList
                                    },
                                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                                    label = { Text("Detail Point", color = ComposeColor(0xFFFFFFFF)) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                        unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                        focusedTextColor = ComposeColor(0xFFFFFFFF),
                                        unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                        cursorColor = ComposeColor(0xFFFFFFFF)
                                    )
                                )
                                IconButton(onClick = {
                                    val updatedProject = project.copy()
                                    updatedProject.description.removeAt(descIndex)
                                    val updatedList = projectsList.toMutableList()
                                    updatedList[projIndex] = updatedProject
                                    projectsList = updatedList
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Point", tint = ComposeColor(0xFFFFFFFF))
                                }
                            }
                        }
                        Button(
                            onClick = {
                                val updatedProject = project.copy()
                                updatedProject.description.add("")
                                val updatedList = projectsList.toMutableList()
                                updatedList[projIndex] = updatedProject
                                projectsList = updatedList
                            },
                            modifier = Modifier
                                .align(Alignment.Start)
                                .background(
                                    brush = Brush.horizontalGradient(colors = buttonGradient),
                                    shape = ButtonDefaults.shape
                                )
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Transparent),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Point", tint = ComposeColor(0xFFFFFFFF))
                            Text(
                                text = "Add Detail Point",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = ComposeColor(0xFFFFFFFF)
                            )
                        }
                        if (projectsList.size > 1) {
                            Button(
                                onClick = {
                                    projectsList = projectsList.filterIndexed { i, _ -> i != projIndex }
                                },
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .background(
                                        brush = Brush.horizontalGradient(colors = buttonGradient),
                                        shape = ButtonDefaults.shape
                                    )
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Transparent),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Project", tint = ComposeColor(0xFFFFFFFF))
                                Text(
                                    text = "Delete Project",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ComposeColor(0xFFFFFFFF)
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = { projectsList = projectsList + Project() },
                    modifier = Modifier
                        .align(Alignment.End)
                        .background(
                            brush = Brush.horizontalGradient(colors = buttonGradient),
                            shape = ButtonDefaults.shape
                        )
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Transparent),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = ComposeColor(0xFFFFFFFF))
                    Text(
                        text = "Add Project",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = ComposeColor(0xFFFFFFFF)
                    )
                }
            }

            // Certificates Section
            SectionHeader("Certificates")
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                certificatesList.forEachIndexed { index, certificate ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = certificate.name,
                            onValueChange = {
                                val updatedList = certificatesList.toMutableList()
                                updatedList[index] = certificate.copy(name = it)
                                certificatesList = updatedList
                            },
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            label = { Text("Certificate Name", color = ComposeColor(0xFFFFFFFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                focusedTextColor = ComposeColor(0xFFFFFFFF),
                                unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                cursorColor = ComposeColor(0xFFFFFFFF)
                            )
                        )
                        if (certificatesList.size > 1) {
                            IconButton(onClick = {
                                certificatesList = certificatesList.filterIndexed { i, _ -> i != index }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ComposeColor(0xFFFFFFFF))
                            }
                        } else {
                            Spacer(modifier = Modifier.width(40.dp))
                        }
                    }
                }
                Button(
                    onClick = { certificatesList = certificatesList + Certificate() },
                    modifier = Modifier
                        .align(Alignment.End)
                        .background(
                            brush = Brush.horizontalGradient(colors = buttonGradient),
                            shape = ButtonDefaults.shape
                        )
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Transparent),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = ComposeColor(0xFFFFFFFF))
                    Text(
                        text = "Add Certificate",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = ComposeColor(0xFFFFFFFF)
                    )
                }
            }

            // Save Dialog (unchanged logic, styled buttons)
            var showDialog by remember { mutableStateOf(false) }
            var fileName by remember { mutableStateOf("") }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Save Resume As", color = ComposeColor(0xFFFFFFFF)) },
                    text = {
                        OutlinedTextField(
                            value = fileName,
                            onValueChange = { fileName = it },
                            label = { Text("Enter file name", color = ComposeColor(0xFFFFFFFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ComposeColor(0xFFFFFFFF),
                                unfocusedBorderColor = ComposeColor(0xFFCCCCCC),
                                focusedTextColor = ComposeColor(0xFFFFFFFF),
                                unfocusedTextColor = ComposeColor(0xFFFFFFFF),
                                cursorColor = ComposeColor(0xFFFFFFFF)
                            )
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (fileName.isNotBlank()) {
                                    generatePDF(
                                        context = context,
                                        name = name,
                                        email = email,
                                        phone = phone,
                                        location = location,
                                        linkedin = linkedin,
                                        objective = objective,
                                        educationList = educationList,
                                        skillsList = skillsList,
                                        projectsList = projectsList,
                                        certificatesList = certificatesList,
                                        fileName = fileName
                                    )
                                    showDialog = false
                                } else {
                                    Toast.makeText(context, "File name cannot be empty", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(colors = buttonGradient),
                                    shape = ButtonDefaults.shape
                                )
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Transparent),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "Save",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = ComposeColor(0xFFFFFFFF)
                            )
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(colors = buttonGradient),
                                    shape = ButtonDefaults.shape
                                )
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Transparent),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "Cancel",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = ComposeColor(0xFFFFFFFF)
                            )
                        }
                    },
                    containerColor = ComposeColor(0xFF252424) // Dark navy background for dialog
                )
            }

            // Create Resume Button
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(58.dp)
                    .background(
                        brush = Brush.horizontalGradient(colors = buttonGradient),
                        shape = ButtonDefaults.shape
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Transparent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Create Resume",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = ComposeColor(0xFFFFFFFF)
                )
            }
        }
    }
}

// Helper Composables
@Composable
fun SectionHeader(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ComposeColor(0xFFFFFFFF),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Divider(color = ComposeColor(0xFFFFFFFF), thickness = 1.dp)
    }
}

// PDF Generation (unchanged)
fun generatePDF(
    context: Context,
    name: String,
    email: String,
    phone: String,
    location: String,
    linkedin: String,
    objective: String,
    educationList: List<Education>,
    skillsList: List<Skill>,
    projectsList: List<Project>,
    certificatesList: List<Certificate>,
    fileName: String
) {
    val pdfDocument = PdfDocument()

    val namePaint = Paint().apply {
        textSize = 24f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
        color = android.graphics.Color.BLACK
    }

    val headingPaint = Paint().apply {
        textSize = 14f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = android.graphics.Color.BLACK
    }

    val subheadingPaint = Paint().apply {
        textSize = 12f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = android.graphics.Color.BLACK
    }

    val contentPaint = Paint().apply {
        textSize = 10f
        color = android.graphics.Color.BLACK
    }

    val linkPaint = Paint().apply {
        textSize = 10f
        color = android.graphics.Color.BLACK
        isUnderlineText = false
    }

    val linePaint = Paint().apply {
        color = android.graphics.Color.BLACK
        strokeWidth = 1f
    }

    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    var page = pdfDocument.startPage(pageInfo)

    val margin = 40f
    val pageWidth = pageInfo.pageWidth.toFloat()
    val pageHeight = pageInfo.pageHeight.toFloat()
    val contentWidth = pageWidth - (2 * margin)

    var yPosition = 50f

    fun createNewPageIfNeeded(): PdfDocument.Page {
        if (yPosition > pageHeight - margin) {
            pdfDocument.finishPage(page)
            page = pdfDocument.startPage(pageInfo)
            yPosition = 50f
            return page
        }
        return page
    }

    page.canvas.drawText(name.uppercase(), pageWidth / 2, yPosition, namePaint)
    yPosition += 25

    val contactInfo = StringBuilder()
    if (phone.isNotEmpty()) contactInfo.append("+91$phone")
    if (location.isNotEmpty()) {
        if (contactInfo.isNotEmpty()) contactInfo.append(" | ")
        contactInfo.append(location)
    }

    if (contactInfo.isNotEmpty()) {
        val contactWidth = contentPaint.measureText(contactInfo.toString())
        val contactX = (pageWidth - contactWidth) / 2
        page.canvas.drawText(contactInfo.toString(), contactX, yPosition, contentPaint)
        yPosition += 15
    }

    if (email.isNotEmpty() || linkedin.isNotEmpty()) {
        val linkText = StringBuilder()
        if (email.isNotEmpty()) linkText.append("Email: $email")
        if (linkedin.isNotEmpty()) {
            if (linkText.isNotEmpty()) linkText.append(" | ")
            linkText.append("LinkedIn: $linkedin")
        }

        val linkWidth = linkPaint.measureText(linkText.toString())
        val linkX = (pageWidth - linkWidth) / 2
        page.canvas.drawText(linkText.toString(), linkX, yPosition, linkPaint)
        yPosition += 25
    }

    if (objective.isNotEmpty()) {
        page = createNewPageIfNeeded()
        page.canvas.drawText("OBJECTIVE", margin, yPosition, headingPaint)
        yPosition += 5
        page.canvas.drawLine(margin, yPosition, pageWidth - margin, yPosition, linePaint)
        yPosition += 15
        yPosition = drawWrappedText(page.canvas, objective, margin, yPosition, contentPaint, contentWidth)
        yPosition += 20
    }

    if (educationList.isNotEmpty() && educationList.any { it.course.isNotEmpty() || it.institution.isNotEmpty() }) {
        page = createNewPageIfNeeded()
        page.canvas.drawText("EDUCATION", margin, yPosition, headingPaint)
        yPosition += 4
        page.canvas.drawLine(margin, yPosition, pageWidth - margin, yPosition, linePaint)
        yPosition += 8

        val smallColWidth = contentWidth * 0.18f
        val largeColWidth = contentWidth * 0.46f
        val colWidths = arrayOf(smallColWidth, largeColWidth, smallColWidth, smallColWidth)

        val headers = arrayOf("Course", "Institution", "Year", "Percentage")

        val thinBorderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 0.5f
            color = Color.BLACK
        }

        val thickBorderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 0.5f
            color = Color.BLACK
        }

        val tableTop = yPosition
        val rowHeight = 15f

        contentPaint.isFakeBoldText = true
        var currentX = margin
        for ((i, header) in headers.withIndex()) {
            val headerWidth = contentPaint.measureText(header)
            val headerX = currentX + ((colWidths[i] - headerWidth) / 2)
            page.canvas.drawText(header, headerX, yPosition + rowHeight / 1.5f, contentPaint)
            currentX += colWidths[i]
        }
        yPosition += rowHeight
        contentPaint.isFakeBoldText = false

        for (edu in educationList) {
            if (edu.course.isNotEmpty() || edu.institution.isNotEmpty() || edu.yearOfPassing.isNotEmpty() || edu.percentage.isNotEmpty()) {
                val fields = arrayOf(edu.course, edu.institution, edu.yearOfPassing, edu.percentage)
                currentX = margin
                for ((i, field) in fields.withIndex()) {
                    val fieldText = if (field.isNotEmpty()) field else "-"
                    val fieldWidth = contentPaint.measureText(fieldText)
                    val fieldX = currentX + ((colWidths[i] - fieldWidth) / 2)
                    page = createNewPageIfNeeded()
                    page.canvas.drawText(fieldText, fieldX, yPosition + rowHeight / 1.5f, contentPaint)
                    currentX += colWidths[i]
                }
                page = createNewPageIfNeeded()
                yPosition += rowHeight
            }
        }

        val tableBottom = yPosition
        page.canvas.drawRect(margin, tableTop, pageWidth - margin, tableBottom, thinBorderPaint)

        currentX = margin
        for (i in 1..3) {
            currentX += colWidths[i - 1]
            val borderPaint = if (i == 1) thickBorderPaint else thinBorderPaint
            page.canvas.drawLine(currentX, tableTop, currentX, tableBottom, borderPaint)
        }

        var currentY = tableTop
        while (currentY <= tableBottom) {
            page.canvas.drawLine(margin, currentY, pageWidth - margin, currentY, thinBorderPaint)
            currentY += rowHeight
        }

        page = createNewPageIfNeeded()
        yPosition += 30
    }

    if (skillsList.isNotEmpty() && skillsList.any { it.category.isNotEmpty() }) {
        page = createNewPageIfNeeded()
        page.canvas.drawText("SKILLS", margin, yPosition, headingPaint)
        yPosition += 5
        page.canvas.drawLine(margin, yPosition, pageWidth - margin, yPosition, linePaint)
        yPosition += 15

        for (skill in skillsList) {
            if (skill.category.isNotEmpty()) {
                val categoryText = "${skill.category}:"
                val categoryWidth = subheadingPaint.measureText(categoryText)
                page.canvas.drawText(categoryText, margin, yPosition, subheadingPaint)

                var currentX = margin + categoryWidth + 10f

                if (skill.items.isNotEmpty()) {
                    val skills = skill.items.split(",").map { it.trim() }.filter { it.isNotEmpty() }

                    for ((i, skillItem) in skills.withIndex()) {
                        val skillText = "• $skillItem"
                        val textWidth = contentPaint.measureText(skillText)

                        if (currentX + textWidth > pageWidth - margin) {
                            page = createNewPageIfNeeded()
                            page.canvas.drawText(skillText, margin + 20f, yPosition, contentPaint)
                            yPosition += contentPaint.textSize + 3
                            currentX = margin + 20f
                        } else {
                            page.canvas.drawText(skillText, currentX, yPosition, contentPaint)
                            currentX += textWidth + 12f
                        }
                    }

                    page = createNewPageIfNeeded()
                    yPosition += contentPaint.textSize + 5
                }
                page = createNewPageIfNeeded()
                yPosition += 6
            }
        }
        page = createNewPageIfNeeded()
        yPosition += 5
    }

    if (projectsList.isNotEmpty() && projectsList.any { it.title.isNotEmpty() }) {
        page = createNewPageIfNeeded()
        page.canvas.drawText("PROJECTS", margin, yPosition, headingPaint)
        yPosition += 5
        page.canvas.drawLine(margin, yPosition, pageWidth - margin, yPosition, linePaint)
        yPosition += 20

        for (project in projectsList) {
            if (project.title.isNotEmpty()) {
                val projectTitle = if (project.technologies.isNotEmpty()) {
                    "${project.title} (${project.technologies})"
                } else {
                    project.title
                }

                page = createNewPageIfNeeded()
                page.canvas.drawText(projectTitle, margin, yPosition, subheadingPaint)
                yPosition += 15

                for (point in project.description) {
                    if (point.isNotEmpty()) {
                        page = createNewPageIfNeeded()
                        page.canvas.drawText("—", margin, yPosition, contentPaint)
                        yPosition = drawWrappedText(page.canvas, point, margin + 20, yPosition, contentPaint, contentWidth - 20)
                        yPosition += 5
                    }
                }
                page = createNewPageIfNeeded()
                yPosition += 10
            }
        }
    }

    if (certificatesList.isNotEmpty() && certificatesList.any { it.name.isNotEmpty() }) {
        page = createNewPageIfNeeded()
        page.canvas.drawText("CERTIFICATES", margin, yPosition, headingPaint)
        yPosition += 5
        page.canvas.drawLine(margin, yPosition, pageWidth - margin, yPosition, linePaint)
        yPosition += 20

        for (cert in certificatesList) {
            if (cert.name.isNotEmpty()) {
                page = createNewPageIfNeeded()
                page.canvas.drawText("•", margin, yPosition, contentPaint)
                yPosition = drawWrappedText(page.canvas, cert.name, margin + 20, yPosition, contentPaint, contentWidth - 20)
                yPosition += 10
            }
        }
    }

    pdfDocument.finishPage(page)

    val finalFileName = if (fileName.endsWith(".pdf")) fileName else "$fileName.pdf"
    val filePath = java.io.File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), finalFileName)

    try {
        pdfDocument.writeTo(java.io.FileOutputStream(filePath))
        pdfDocument.close()
        Toast.makeText(context, "PDF saved as $finalFileName", Toast.LENGTH_LONG).show()
        openPDF(context, filePath)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF", Toast.LENGTH_LONG).show()
    }
}

// Text Wrapping for PDF (unchanged)
fun drawWrappedText(canvas: Canvas, text: String, x: Float, y: Float, paint: Paint, maxWidth: Float): Float {
    var yPos = y
    val lines = wrapText(text, paint, maxWidth)

    for (line in lines) {
        canvas.drawText(line, x, yPos, paint)
        yPos += paint.textSize + 3
    }

    return yPos
}

fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
    val result = mutableListOf<String>()
    val words = text.split(" ")
    var currentLine = StringBuilder()

    for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "${currentLine} $word"
        val measureWidth = paint.measureText(testLine)

        if (measureWidth <= maxWidth) {
            currentLine = StringBuilder(testLine)
        } else {
            if (currentLine.isNotEmpty()) {
                result.add(currentLine.toString())
                currentLine = StringBuilder(word)
            } else {
                result.add(word)
                currentLine = StringBuilder()
            }
        }
    }

    if (currentLine.isNotEmpty()) {
        result.add(currentLine.toString())
    }

    return result
}

// Open PDF (unchanged)
fun openPDF(context: Context, file: java.io.File) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No PDF Viewer Installed", Toast.LENGTH_LONG).show()
    }
}