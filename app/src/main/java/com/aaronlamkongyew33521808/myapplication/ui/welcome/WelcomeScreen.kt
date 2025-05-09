package com.aaronlamkongyew33521808.myapplication.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaronlamkongyew33521808.myapplication.R

@Composable
fun WelcomeScreen(onLetsGo: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(100.dp))
            Text(
                "NutriTrack",
                style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(32.dp))
            Image(
                painter = painterResource(R.drawable.egg_logo),
                contentDescription = "NutriTrack Logo",
                modifier = Modifier.size(250.dp)
            )
            Spacer(Modifier.height(32.dp))
            Text(
                """
        This app provides general health and nutrition information for educational purposes only. 
        It is not intended as medical advice. Always consult a healthcare professional before changes.
        """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onLetsGo,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Let's Go!")
            }
        }
    }
}
