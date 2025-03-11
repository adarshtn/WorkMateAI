package com.example.workmateai

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workmateai.ui.theme.WorkMateAITheme
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.PI
import kotlin.math.sin

class RegisterActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            auth = FirebaseAuth.getInstance()
            Log.d("RegisterActivity", "FirebaseAuth initialized successfully")
        } catch (e: Exception) {
            Log.e("RegisterActivity", "FirebaseAuth initialization failed: ${e.message}", e)
            setContent {
                WorkMateAITheme {
                    Text("Firebase initialization failed: ${e.message}")
                }
            }
            return
        }
        setContent {
            WorkMateAITheme {
                RegisterScreen(auth)
            }
        }
    }
}

@Composable
fun RegisterScreen(auth: FirebaseAuth) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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
            // Title styled like WelcomeActivity
            Text(
                text = "Register",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF),
                modifier = Modifier.padding(bottom = 50.dp)
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = "" },
                label = { Text("Email", color = Color(0xFFFFFFFF)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFFFFF),
                    unfocusedBorderColor = Color(0xFFCCCCCC),
                    focusedTextColor = Color(0xFFFFFFFF),
                    unfocusedTextColor = Color(0xFFFFFFFF),
                    cursorColor = Color(0xFFFFFFFF)
                )
            )

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = "" },
                label = { Text("Password", color = Color(0xFFFFFFFF)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFFFFF),
                    unfocusedBorderColor = Color(0xFFCCCCCC),
                    focusedTextColor = Color(0xFFFFFFFF),
                    unfocusedTextColor = Color(0xFFFFFFFF),
                    cursorColor = Color(0xFFFFFFFF)
                )
            )

            // Error message
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Register button styled like WelcomeActivity
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        Log.d("RegisterActivity", "Attempting registration with email: $email")
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("RegisterActivity", "Registration successful")
                                    context.startActivity(Intent(context, HomeActivity::class.java))
                                    (context as? ComponentActivity)?.finish()
                                } else {
                                    errorMessage = task.exception?.message ?: "Registration failed"
                                    Log.e("RegisterActivity", "Registration failed: $errorMessage")
                                }
                            }
                    } else {
                        errorMessage = "Please fill all fields"
                        Log.w("RegisterActivity", "Fields empty")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
                    .height(58.dp)
                    .background(
                        brush = Brush.horizontalGradient(colors = buttonGradient),
                        shape = ButtonDefaults.shape
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Register",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}