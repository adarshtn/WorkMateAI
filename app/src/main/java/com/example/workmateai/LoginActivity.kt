package com.example.workmateai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.workmateai.ui.theme.WorkMateAITheme
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.PI
import kotlin.math.sin

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            auth = FirebaseAuth.getInstance()
            Log.d("LoginActivity", "FirebaseAuth initialized successfully")
        } catch (e: Exception) {
            Log.e("LoginActivity", "FirebaseAuth initialization failed: ${e.message}", e)
            setContent {
                WorkMateAITheme {
                    Text("Firebase initialization failed: ${e.message}")
                }
            }
            return
        }
        setContent {
            WorkMateAITheme {
                LoginScreen(auth)
            }
        }
    }
}

@Composable
fun LoginScreen(auth: FirebaseAuth) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var showResetDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }
    var resetErrorMessage by remember { mutableStateOf("") }

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

    // Custom themed password reset dialog
    if (showResetDialog) {
        Dialog(onDismissRequest = { showResetDialog = false }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = gradientColors,
                            start = androidx.compose.ui.geometry.Offset(
                                x = gradientSize * waveOffset * 0.1f,
                                y = gradientSize * 0.1f * secondWave
                            ),
                            end = androidx.compose.ui.geometry.Offset(
                                x = gradientSize * (1f - waveOffset) * 0.1f,
                                y = gradientSize * (0.1f + 0.1f * secondWave)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Reset Password",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFFFFF),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Enter your email address to receive a password reset link",
                        color = Color(0xFFCCCCCC),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = {
                            resetEmail = it
                            resetErrorMessage = ""
                        },
                        label = { Text("Email", color = Color(0xFFFFFFFF)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            focusedTextColor = Color(0xFFFFFFFF),
                            unfocusedTextColor = Color(0xFFFFFFFF),
                            cursorColor = Color(0xFFFFFFFF)
                        )
                    )

                    // Error message in dialog
                    if (resetErrorMessage.isNotEmpty()) {
                        Text(
                            text = resetErrorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Cancel button
                        TextButton(onClick = { showResetDialog = false }) {
                            Text(
                                text = "Cancel",
                                color = Color(0xFFCCCCCC),
                                fontSize = 16.sp
                            )
                        }

                        // Send button with same gradient as login button
                        Button(
                            onClick = {
                                if (resetEmail.isNotEmpty()) {
                                    auth.sendPasswordResetEmail(resetEmail)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d("LoginActivity", "Password reset email sent")
                                                successMessage = "Password reset email sent to $resetEmail"
                                                errorMessage = ""
                                                showResetDialog = false
                                            } else {
                                                resetErrorMessage = task.exception?.message ?: "Failed to send reset email"
                                                Log.e("LoginActivity", "Failed to send reset email: $resetErrorMessage")
                                            }
                                        }
                                } else {
                                    resetErrorMessage = "Please enter your email"
                                }
                            },
                            modifier = Modifier
                                .height(50.dp)
                                .background(
                                    brush = Brush.horizontalGradient(colors = buttonGradient),
                                    shape = ButtonDefaults.shape
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "Send Reset Link",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFFFFFFF)
                            )
                        }
                    }
                }
            }
        }
    }

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
                text = "Login",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF),
                modifier = Modifier.padding(bottom = 50.dp)
            )

            // Email field (keeping functionality, styled minimally)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = ""; successMessage = "" },
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
                onValueChange = { password = it; errorMessage = ""; successMessage = "" },
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

            // Success message
            if (successMessage.isNotEmpty()) {
                Text(
                    text = successMessage,
                    color = Color(0xFF4CAF50), // Green color for success
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Login button styled like WelcomeActivity
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        Log.d("LoginActivity", "Attempting login with email: $email")
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("LoginActivity", "Login successful")
                                    context.startActivity(Intent(context, HomeActivity::class.java))
                                    (context as? ComponentActivity)?.finish()
                                } else {
                                    errorMessage = task.exception?.message ?: "Login failed"
                                    Log.e("LoginActivity", "Login failed: $errorMessage")
                                }
                            }
                    } else {
                        errorMessage = "Please fill all fields"
                        Log.w("LoginActivity", "Fields empty")
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
                    text = "Login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFFFFF)
                )
            }

            // Forgot Password button
            TextButton(
                onClick = {
                    showResetDialog = true
                    resetEmail = email // Pre-fill with current email if available
                    resetErrorMessage = ""
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Forgot Password?",
                    color = Color(0xFFCCCCCC),
                    fontSize = 14.sp
                )
            }
        }
    }
}