package com.example.workmateai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.workmateai.ui.theme.WorkMateAITheme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkMateAITheme {
                WelcomeScreen()
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    val context = LocalContext.current

    // Get the current color scheme
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to WorkMate AI",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground, // This changes color based on system theme
            modifier = Modifier.padding(bottom = 30.dp)
        )

        Button(
            onClick = {
                context.startActivity(Intent(context, LoginActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        ) {
            Text(
                text = "Login",
                fontSize = 18.sp
            )
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, RegisterActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Register",
                fontSize = 18.sp
            )
        }
    }
}
