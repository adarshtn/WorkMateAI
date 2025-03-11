package com.example.workmateai.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workmateai.Job
import com.example.workmateai.ui.theme.WorkMateAITheme
import kotlin.math.PI
import kotlin.math.sin

class JobResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jobList = intent.getParcelableArrayListExtra<Job>("JOB_LIST") ?: arrayListOf()
        val enhancedSkills = intent.getStringArrayListExtra("ENHANCED_SKILLS") ?: arrayListOf()

        setContent {
            WorkMateAITheme {
                JobResultsScreen(jobList = jobList, enhancedSkills = enhancedSkills)
            }
        }
    }
}

@Composable
fun JobResultsScreen(jobList: List<Job>, enhancedSkills: List<String>) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title styled like WelcomeActivity
            Text(
                text = "Job Suggestions",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF),
                modifier = Modifier.padding(bottom = 50.dp)
            )

            if (jobList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        EnhancedSkillsSection(enhancedSkills = enhancedSkills)
                    }
                    items(jobList) { job ->
                        JobCard(job = job, buttonGradient = buttonGradient)
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No job suggestions found.",
                        fontSize = 18.sp,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedSkillsSection(enhancedSkills: List<String>) {
    if (enhancedSkills.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = " AI Enhanced Skills",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF)
            )
            Text(
                text = enhancedSkills.joinToString(", "),
                fontSize = 16.sp,
                color = Color(0xFFCCCCCC),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun JobCard(job: Job, buttonGradient: List<Color>) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth(), // Removed clickable modifier
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252424)), // Dark navy
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = job.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF)
            )
            Text(
                text = job.company,
                fontSize = 16.sp,
                color = Color(0xFFCCCCCC),
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = job.location,
                fontSize = 14.sp,
                color = Color(0xFFCCCCCC),
                modifier = Modifier.padding(top = 4.dp)
            )
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.link))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 12.dp)
                    .background(
                        brush = Brush.horizontalGradient(colors = buttonGradient),
                        shape = ButtonDefaults.shape
                    )
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Apply",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}