package com.example.workmateai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.workmateai.ui.theme.WorkMateAITheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase to avoid crashes
        try {
            FirebaseApp.initializeApp(this)
            Log.d("MainActivity", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("MainActivity", "Firebase initialization failed: ${e.message}", e)
        }

        setContent {
            WorkMateAITheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to WorkMate AI",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (isLoggedIn) {
            Button(
                onClick = {
                    auth.signOut()
                    isLoggedIn = false
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Logout")
            }
        } else {
            Button(
                onClick = {
                    navigateToScreen(context, LoginActivity::class.java)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Login")
            }

            Button(
                onClick = {
                    navigateToScreen(context, RegisterActivity::class.java)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Register")
            }
        }
    }
}

// Function to navigate safely between screens
private fun navigateToScreen(context: android.content.Context, destination: Class<*>) {
    try {
        context.startActivity(Intent(context, destination))
    } catch (e: Exception) {
        Log.e("MainActivity", "Navigation error: ${e.message}", e)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    WorkMateAITheme {
        MainScreen()
    }
}
