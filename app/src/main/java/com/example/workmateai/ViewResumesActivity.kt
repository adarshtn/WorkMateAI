package com.example.workmateai

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.workmateai.ui.theme.WorkMateAITheme
import java.io.File
import kotlin.math.PI
import kotlin.math.sin

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
        Color(0xFF000000), // Pure Black
        Color(0xFF434343)  // Near-black with hint of blue
    )
    val buttonGradient = listOf(
        Color(0xFF5B5959), // Midnight navy
        Color(0xFF252424)  // Deep navy
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
            // Title styled like WelcomeActivity
            Text(
                text = "Saved Resumes",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF),
                modifier = Modifier.padding(bottom = 50.dp)
            )

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
fun ResumeCard(file: File, onView: () -> Unit, onDelete: () -> Unit, buttonGradient: List<Color>) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onView() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252424)), // Dark navy
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
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF)
                )
                Text(
                    text = "Tap to view",
                    fontSize = 14.sp,
                    color = Color(0xFFCCCCCC)
                )
            }

            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(colors = buttonGradient),
                        shape = ButtonDefaults.shape
                    )
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Delete",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }

    // Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Deletion", color = Color(0xFFFFFFFF)) },
            text = { Text("Are you sure you want to delete ${file.name}?", color = Color(0xFFCCCCCC)) },
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
                            .background(
                                brush = Brush.horizontalGradient(colors = buttonGradient),
                                shape = ButtonDefaults.shape
                            )
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFFFFFFF)
                        )
                    }
                    Button(
                        onClick = {
                            showDialog = false
                            onDelete()
                        },
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(colors = buttonGradient),
                                shape = ButtonDefaults.shape
                            )
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = "Delete",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFFFFFFF)
                        )
                    }
                }
            },
            containerColor = Color(0xFF252424) // Dark navy background for dialog
        )
    }
}
