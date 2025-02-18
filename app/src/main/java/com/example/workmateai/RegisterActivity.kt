package com.example.workmateai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import com.example.workmateai.ui.theme.WorkMateAITheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkMateAITheme {
                RegisterScreen()
            }
        }
    }
}

@Composable
fun RegisterScreen() {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Use a Box to center-align the content
    Box(
        modifier = Modifier
            .fillMaxSize() // Fills the screen
            .padding(16.dp), // Adds padding around the content
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Centers the content vertically and horizontally
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, // Centers children horizontally
            verticalArrangement = Arrangement.Center // Vertically centers the elements
        ) {
            Text(
                "Register",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black // Set text color to black
                )
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )

            Button(
                onClick = {
                    context.startActivity(Intent(context, HomeActivity::class.java))
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Register")
            }
        }
    }
}
