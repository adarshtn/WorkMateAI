package com.example.workmateai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.workmateai.ui.theme.WorkMateAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkMateAITheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current // Use LocalContext to get the context

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                try {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                } catch (e: Exception) {
                    // Handle exceptions and show an error message
                    e.printStackTrace() // Log the error for debugging
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Login")
        }

        Button(
            onClick = {
                try {
                    context.startActivity(Intent(context, RegisterActivity::class.java))
                } catch (e: Exception) {
                    // Handle exceptions and show an error message
                    e.printStackTrace() // Log the error for debugging
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    WorkMateAITheme {
        MainScreen()
    }
}
