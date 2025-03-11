package com.example.workmateai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import kotlin.math.PI
import kotlin.math.sin

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Name styled like WelcomeActivity
            Text(
                text = "WorkMate AI",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF),
                modifier = Modifier.padding(bottom = 50.dp)
            )

            // Standard button modifier with WelcomeActivity styling
            val buttonModifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp) // Adjusted for spacing between buttons
                .height(58.dp)
                .background(
                    brush = Brush.horizontalGradient(colors = buttonGradient),
                    shape = ButtonDefaults.shape
                )

            // Create Resume Button
            Button(
                onClick = { context.startActivity(Intent(context, CreateResumeActivity::class.java)) },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Create Resume",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFFFFF)
                )
            }

            // View Saved Resumes Button
            Button(
                onClick = { context.startActivity(Intent(context, ViewResumesActivity::class.java)) },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "View Saved Resumes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFFFFF)
                )
            }

            // Job Suggestions Button
            Button(
                onClick = { context.startActivity(Intent(context, JobSuggestionsActivity::class.java)) },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Job Suggestions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}