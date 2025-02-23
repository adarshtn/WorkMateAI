package com.example.workmateai

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workmateai.ui.theme.WorkMateAITheme
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.FileProvider

// Data classes
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

// Activity
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

// Main Composable
@Composable
fun CreateResumeScreen() {
    val context = LocalContext.current

    // State for each field
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Create Resume",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = ComposeColor.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),

        )

        // Personal Information Section
        SectionHeader("Personal Information")
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
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
        OutlinedTextField(
            value = linkedin,
            onValueChange = { linkedin = it },
            label = { Text("LinkedIn Profile") },
            modifier = Modifier.fillMaxWidth()
        )

        // Objective Section
        SectionHeader("Objective")
        OutlinedTextField(
            value = objective,
            onValueChange = { objective = it },
            label = { Text("Professional Objective") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        // Education Section
        SectionHeader("Education")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Keep padding for consistent spacing
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Education Entries
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
                        label = { Text("Course") }
                    )
                    OutlinedTextField(
                        value = education.institution,
                        onValueChange = {
                            val updatedList = educationList.toMutableList()
                            updatedList[index] = education.copy(institution = it)
                            educationList = updatedList
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Institution") }
                    )
                    OutlinedTextField(
                        value = education.yearOfPassing,
                        onValueChange = {
                            val updatedList = educationList.toMutableList()
                            updatedList[index] = education.copy(yearOfPassing = it)
                            educationList = updatedList
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Year") }
                    )
                    OutlinedTextField(
                        value = education.percentage,
                        onValueChange = {
                            val updatedList = educationList.toMutableList()
                            updatedList[index] = education.copy(percentage = it)
                            educationList = updatedList
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("%") }
                    )
                    if (educationList.size > 1) {
                        IconButton(
                            onClick = {
                                educationList = educationList.filterIndexed { i, _ -> i != index }
                            },
                            modifier = Modifier.align(Alignment.End).size(24.dp) // Align delete button to the end (right)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
            Button(
                onClick = { educationList = educationList + Education() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Add Education")
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
                        label = { Text("Category") }
                    )
                    OutlinedTextField(
                        value = skill.items,
                        onValueChange = {
                            val updatedList = skillsList.toMutableList()
                            updatedList[index] = skill.copy(items = it)
                            skillsList = updatedList
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Skills (comma separated)") }
                    )
                    IconButton(
                        onClick = {
                            skillsList = skillsList.filterIndexed { i, _ -> i != index }
                        },
                        modifier = Modifier.align(Alignment.End).size(24.dp) // Align delete button to the end (right)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
            Button(
                onClick = { skillsList = skillsList + Skill() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Add Skill Category")
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
                        .border(1.dp, ComposeColor.LightGray)
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
                        label = { Text("Project Title") }
                    )
                    OutlinedTextField(
                        value = project.technologies,
                        onValueChange = {
                            val updatedList = projectsList.toMutableList()
                            updatedList[projIndex] = project.copy(technologies = it)
                            projectsList = updatedList
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Technologies Used (comma separated)") }
                    )
                    Text(
                        text = "Project Details:",
                        modifier = Modifier.padding(top = 8.dp),
                        fontWeight = FontWeight.Bold
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
                                label = { Text("Detail Point") }
                            )
                            IconButton(onClick = {
                                val updatedProject = project.copy()
                                updatedProject.description.removeAt(descIndex)
                                val updatedList = projectsList.toMutableList()
                                updatedList[projIndex] = updatedProject
                                projectsList = updatedList
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Point")
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
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Point")
                        Text("Add Detail Point")
                    }
                    if (projectsList.size > 1) {
                        Button(
                            onClick = {
                                projectsList = projectsList.filterIndexed { i, _ -> i != projIndex }
                            },
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Red)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Project")
                            Text("Delete Project")
                        }
                    }
                }
            }
            Button(
                onClick = { projectsList = projectsList + Project() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Add Project")
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
                        label = { Text("Certificate Name") }
                    )
                    if (certificatesList.size > 1) {
                        IconButton(onClick = {
                            certificatesList = certificatesList.filterIndexed { i, _ -> i != index }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(40.dp))
                    }
                }
            }
            Button(
                onClick = { certificatesList = certificatesList + Certificate() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Add Certificate")
            }
        }

        // Save Dialog
        var showDialog by remember { mutableStateOf(false) }
        var fileName by remember { mutableStateOf("") }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Save Resume As") },
                text = {
                    OutlinedTextField(
                        value = fileName,
                        onValueChange = { fileName = it },
                        label = { Text("Enter file name") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
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
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Create Resume")
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
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Divider(color = ComposeColor.Black, thickness = 1.dp)
    }
}

// PDF Generation
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
        color = android.graphics.Color.BLACK // Plain text, no link appearance
        isUnderlineText = false // No underline for plain text
    }

    val linePaint = Paint().apply {
        color = android.graphics.Color.BLACK
        strokeWidth = 1f
    }

    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size (595 width, 842 height in points)
    var page = pdfDocument.startPage(pageInfo)

    val margin = 40f
    val pageWidth = pageInfo.pageWidth.toFloat()
    val pageHeight = pageInfo.pageHeight.toFloat()
    val contentWidth = pageWidth - (2 * margin)

    var yPosition = 50f // Start the name lower, as requested

    // Function to check if we need a new page and handle page creation
    fun createNewPageIfNeeded(): PdfDocument.Page {
        if (yPosition > pageHeight - margin) {
            pdfDocument.finishPage(page)
            page = pdfDocument.startPage(pageInfo)
            yPosition = 50f // Reset yPosition to top of new page with margin
            return page
        }
        return page
    }

    // Draw name centered horizontally
    page.canvas.drawText(name.uppercase(), pageWidth / 2, yPosition, namePaint)
    yPosition += 25

    // Prepare and draw phone and location centered horizontally, same as name
    val contactInfo = StringBuilder()
    if (phone.isNotEmpty()) contactInfo.append("+91$phone")
    if (location.isNotEmpty()) {
        if (contactInfo.isNotEmpty()) contactInfo.append(" | ")
        contactInfo.append(location)
    }

    if (contactInfo.isNotEmpty()) {
        // Center the contact info horizontally
        val contactWidth = contentPaint.measureText(contactInfo.toString())
        val contactX = (pageWidth - contactWidth) / 2 // Center the text horizontally
        page.canvas.drawText(contactInfo.toString(), contactX, yPosition, contentPaint)
        yPosition += 15
    }

    // Draw "Email:" and email, and "LinkedIn:" and LinkedIn on the same line, centered horizontally
    if (email.isNotEmpty() || linkedin.isNotEmpty()) {
        val linkText = StringBuilder()
        if (email.isNotEmpty()) linkText.append("Email: $email") // Added "Email:" prefix
        if (linkedin.isNotEmpty()) {
            if (linkText.isNotEmpty()) linkText.append(" | ")
            linkText.append("LinkedIn: $linkedin") // Added "LinkedIn:" prefix
        }

        // Measure the width of the link text to center it
        val linkWidth = linkPaint.measureText(linkText.toString())
        val linkX = (pageWidth - linkWidth) / 2 // Center the link text horizontally
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
        yPosition += 4 // Minimal space after heading
        page.canvas.drawLine(margin, yPosition, pageWidth - margin, yPosition, linePaint)
        yPosition += 8 // Very minimal space after the line

        // Define custom column widths
        val smallColWidth = contentWidth * 0.18f // Small columns for Course, Year, Percentage
        val largeColWidth = contentWidth * 0.46f // Larger column for Institution
        val colWidths = arrayOf(smallColWidth, largeColWidth, smallColWidth, smallColWidth)

        val headers = arrayOf("Course", "Institution", "Year", "Percentage")

        // Set up thin border paint
        val thinBorderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 0.5f // Thin for small columns
            color = Color.BLACK
        }

        // Set up thick border paint for Institution column
        val thickBorderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 0.5f // Thicker for Institution
            color = Color.BLACK
        }

        // Calculate table dimensions
        val tableTop = yPosition
        val rowHeight = 15f // Compact row height

        // Apply bold effect temporarily for headers
        contentPaint.isFakeBoldText = true

        // Draw table headers
        var currentX = margin
        for ((i, header) in headers.withIndex()) {
            val headerWidth = contentPaint.measureText(header)
            val headerX = currentX + ((colWidths[i] - headerWidth) / 2)
            page.canvas.drawText(header, headerX, yPosition + rowHeight / 1.5f, contentPaint)
            currentX += colWidths[i]
        }
        yPosition += rowHeight // Move to the next row
        contentPaint.isFakeBoldText = false // Remove bold effect

        // Draw education entries
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
                yPosition += rowHeight // Move to the next row
            }
        }

        // Draw outer border
        val tableBottom = yPosition
        page.canvas.drawRect(margin, tableTop, pageWidth - margin, tableBottom, thinBorderPaint)

        // Draw vertical borders with varying thickness
        currentX = margin
        for (i in 1..3) {
            currentX += colWidths[i - 1]
            val borderPaint = if (i == 1) thickBorderPaint else thinBorderPaint // Institution column has thicker border
            page.canvas.drawLine(currentX, tableTop, currentX, tableBottom, borderPaint)
        }

        // Draw horizontal lines for rows
        var currentY = tableTop
        while (currentY <= tableBottom) {
            page.canvas.drawLine(margin, currentY, pageWidth - margin, currentY, thinBorderPaint)
            currentY += rowHeight
        }

        page = createNewPageIfNeeded()
        yPosition += 30 // Minimal space after the table
    }

    if (skillsList.isNotEmpty() && skillsList.any { it.category.isNotEmpty() }) {
        page = createNewPageIfNeeded()
        page.canvas.drawText("SKILLS", margin, yPosition, headingPaint)
        yPosition += 5
        page.canvas.drawLine(margin, yPosition, pageWidth - margin, yPosition, linePaint)
        yPosition += 15 // Compact spacing

        for (skill in skillsList) {
            if (skill.category.isNotEmpty()) {
                // Draw category and start skills from the same line
                val categoryText = "${skill.category}:"
                val categoryWidth = subheadingPaint.measureText(categoryText)
                page.canvas.drawText(categoryText, margin, yPosition, subheadingPaint)

                var currentX = margin + categoryWidth + 10f // Start skills after category text

                if (skill.items.isNotEmpty()) {
                    val skills = skill.items.split(",").map { it.trim() }.filter { it.isNotEmpty() }

                    // Draw skills on the same line until space runs out
                    for ((i, skillItem) in skills.withIndex()) {
                        val skillText = "• $skillItem"
                        val textWidth = contentPaint.measureText(skillText)

                        // Move to the next line if text exceeds page width
                        if (currentX + textWidth > pageWidth - margin) {
                            page = createNewPageIfNeeded()
                            page.canvas.drawText(skillText, margin + 20f, yPosition, contentPaint) // Start new line
                            yPosition += contentPaint.textSize + 3 // Move to the next line
                            currentX = margin + 20f // Indent for wrapped lines
                        } else {
                            page.canvas.drawText(skillText, currentX, yPosition, contentPaint)
                            currentX += textWidth + 12f // Add spacing between skills
                        }
                    }

                    page = createNewPageIfNeeded()
                    yPosition += contentPaint.textSize + 5 // Space after the skills
                }
                page = createNewPageIfNeeded()
                yPosition += 6 // Space between categories
            }
        }
        page = createNewPageIfNeeded()
        yPosition += 5 // Small space after the skills section
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
    val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), finalFileName)

    try {
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()
        Toast.makeText(context, "PDF saved as $finalFileName", Toast.LENGTH_LONG).show()
        openPDF(context, filePath)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF", Toast.LENGTH_LONG).show()
    }
}
// Text Wrapping for PDF
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

// Open PDF
fun openPDF(context: Context, file: File) {
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