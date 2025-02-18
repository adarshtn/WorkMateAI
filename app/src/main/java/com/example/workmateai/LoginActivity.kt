package com.example.workmateai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import com.example.workmateai.ui.theme.WorkMateAITheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkMateAITheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Dummy credentials for validation (You can replace this with actual validation logic)
    val validUsername = "user123"
    val validPassword = "password123"

    // Get the current color scheme to adjust the heading color for the system theme
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Login",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = colors.onBackground // Changes color according to system theme
            )
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it; errorMessage = "" }, // Clear error message when user starts typing
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = "" }, // Clear error message when user starts typing
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), // Use dots for password
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = {
                // Validate login
                if (username == validUsername && password == validPassword) {
                    context.startActivity(Intent(context, HomeActivity::class.java))
                } else {
                    errorMessage = "Invalid username or password"
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Login")
        }
    }
}
