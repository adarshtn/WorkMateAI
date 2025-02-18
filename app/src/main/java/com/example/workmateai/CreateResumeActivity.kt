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
import android.content.Context
import android.graphics.Paint
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import android.content.Intent
import androidx.core.content.FileProvider
import android.graphics.Typeface


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
            label = { Text("Phone Number/EmailID") },
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
                generatePDF(
                    context = context,
                    name = name,
                    phone = phone,
                    location = location,
                    educationList = educationFields.filter { it.isNotBlank() },
                    skillsList = skillsFields.filter { it.isNotBlank() },
                    experienceList = experienceFields.filter { it.isNotBlank() },
                    projectsList = projectsFields.filter { it.isNotBlank() }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Resume")
        }
    }
}


fun generatePDF(
    context: Context,
    name: String,
    phone: String,
    location: String,
    educationList: List<String>,
    skillsList: List<String>,
    experienceList: List<String>,
    projectsList: List<String>
) {
    val pdfDocument = PdfDocument()

    val namePaint = Paint().apply {
        textSize = 32f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    val subheadingPaint = Paint().apply {
        textSize = 14f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    val contentPaint = Paint().apply {
        textSize = 12f
    }

    val borderPaint = Paint().apply {
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Define margins
    val margin = 30f
    val contentMargin = margin + 20f  // Additional margin for content inside border

    // Draw border
    canvas.drawRect(
        margin,
        margin,
        (pageInfo.pageWidth - margin),
        (pageInfo.pageHeight - margin),
        borderPaint
    )

    var yPosition = 100f

    // Name (Centered)
    canvas.drawText(name, (pageInfo.pageWidth / 2).toFloat(), yPosition, namePaint)
    yPosition += 50

    // Calculate available width for text wrapping
    val availableWidth = pageInfo.pageWidth - (2 * contentMargin)

    // Contact Information
    yPosition = drawWrappedText(canvas, "Contact: $phone", contentMargin, yPosition, contentPaint, availableWidth)
    yPosition = drawWrappedText(canvas, "Location: $location", contentMargin, yPosition, contentPaint, availableWidth)
    yPosition += 20

    // Education Section
    canvas.drawText("Education", contentMargin, yPosition, subheadingPaint)
    yPosition += 20
    for (edu in educationList) {
        yPosition = drawWrappedText(canvas, "- $edu", contentMargin + 10, yPosition, contentPaint, availableWidth - 10)
        yPosition += 10
    }

    // Skills Section
    yPosition += 10
    canvas.drawText("Skills", contentMargin, yPosition, subheadingPaint)
    yPosition += 20
    for (skill in skillsList) {
        yPosition = drawWrappedText(canvas, "- $skill", contentMargin + 10, yPosition, contentPaint, availableWidth - 10)
        yPosition += 10
    }

    // Experience Section
    yPosition += 10
    canvas.drawText("Experience", contentMargin, yPosition, subheadingPaint)
    yPosition += 20
    for (exp in experienceList) {
        yPosition = drawWrappedText(canvas, "- $exp", contentMargin + 10, yPosition, contentPaint, availableWidth - 10)
        yPosition += 10
    }

    // Projects Section
    yPosition += 10
    canvas.drawText("Projects", contentMargin, yPosition, subheadingPaint)
    yPosition += 20
    for (proj in projectsList) {
        yPosition = drawWrappedText(canvas, "- $proj", contentMargin + 10, yPosition, contentPaint, availableWidth - 10)
        yPosition += 10
    }

    pdfDocument.finishPage(page)

    val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Resume.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()
        Toast.makeText(context, "PDF saved at ${filePath.absolutePath}", Toast.LENGTH_LONG).show()
        openPDF(context, filePath)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF", Toast.LENGTH_LONG).show()
    }
}

fun drawWrappedText(canvas: Canvas, text: String, x: Float, y: Float, paint: Paint, maxWidth: Float): Float {
    val words = text.split(" ")
    var currentLine = ""
    var yPos = y

    for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        val textWidth = paint.measureText(testLine)

        if (textWidth > maxWidth) {
            // Justify the current line
            val lineWidth = paint.measureText(currentLine)
            val spaceWidth = paint.measureText(" ")

            val extraSpace = (maxWidth - lineWidth) / (currentLine.split(" ").size - 1)
            var xPos = x

            // Draw each word with additional spaces for justification
            currentLine.split(" ").forEachIndexed { index, word ->
                canvas.drawText(word, xPos, yPos, paint)
                xPos += paint.measureText(word) + extraSpace
            }
            yPos += paint.textSize + 5  // Add a small gap between lines
            currentLine = word
        } else {
            currentLine = testLine
        }
    }

    // Handle the last line (left-aligned)
    if (currentLine.isNotEmpty()) {
        canvas.drawText(currentLine, x, yPos, paint)
        yPos += paint.textSize + 5
    }

    return yPos
}

// Function to Open the PDF File
fun openPDF(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, "application/pdf")
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No PDF Viewer Installed", Toast.LENGTH_LONG).show()
    }
}