package com.aaronlamkongyew33521808.myapplication.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaronlamkongyew33521808.myapplication.R

@Composable
fun WelcomeScreen(onLetsGo: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    Surface(
        modifier = Modifier.fillMaxSize()
        .verticalScroll(rememberScrollState()),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding((screenWidth * 0.04).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height((screenHeight * 0.1).dp))

            Text(
                text = "NutriTrack",
                style = TextStyle(
                    fontSize = (screenWidth * 0.08).sp,
                    fontWeight = FontWeight.Bold
                ),
            )

            Image(
                painter = painterResource(id = R.drawable.egg_logo),
                contentDescription = "NutriTrack Logo",
                modifier = Modifier.size((screenWidth * 0.75).dp)
            )

            Spacer(modifier = Modifier.height((screenHeight * 0.04).dp))

            Text(
                text =
                    """
                                This app provides general health and nutrition information for educational purposes only. It is not intended as medical advice, diagnosis, or treatment. Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen. Use this app at your own risk.

                                If you’d like to an Accredited Practicing Dietitian (APD), please visit the Monash Nutrition/Dietetics Clinic (discounted rates for students):
                                https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition
                                """.trimIndent(),
                textAlign = TextAlign.Justify,
                style = TextStyle(fontSize = (screenWidth * 0.04).sp),
            )

            Spacer(modifier = Modifier.height((screenHeight * 0.04).dp))

            Button(
                onClick = onLetsGo,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Let's Go!",
                    fontSize = (screenWidth * 0.045).sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Designed with ❤️ by Aaron Lam Kong Yew (33521808)",
                style = TextStyle(fontSize = (screenWidth * 0.03).sp),
            )
            Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
        }
    }
}
