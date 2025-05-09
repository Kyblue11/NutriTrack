package com.aaronlamkongyew33521808.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaronlamkongyew33521808.myapplication.ui.navigation.AppNavGraph
import com.aaronlamkongyew33521808.myapplication.ui.theme.NutriTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriTrackTheme {
                AppNavGraph()
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Spacer(modifier = Modifier.height(100.dp))
//
//                        Text(
//                            text = "NutriTrack",
//                            style = TextStyle(
//                                fontSize = 32.sp,
//                                fontWeight = FontWeight.Bold
//                            ),
//                        )
//
//                        Image(
//                            painter = painterResource(id = R.drawable.egg_logo),
//                            contentDescription = "NutriTrack Logo",
//                            modifier = Modifier.size(300.dp)
//                        )
//
//                        Spacer(modifier = Modifier.height(32.dp))
//
//                        Text(
//                            text =
//                                """
//                                This app provides general health and nutrition information for educational purposes only. It is not intended as medical advice, diagnosis, or treatment. Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen. Use this app at your own risk.
//
//                                If you’d like to an Accredited Practicing Dietitian (APD), please visit the Monash Nutrition/Dietetics Clinic (discounted rates for students):
//                                https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition
//                                """.trimIndent(),
//                            textAlign = TextAlign.Justify,
//                            style = TextStyle(fontSize = 16.sp),
//                        )
//
//                        Spacer(modifier = Modifier.height(24.dp))
//
//                        Button(
//                            onClick = {
//                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//                            },
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Text(text = "Let's Go!")
//                        }
//
//                        Spacer(modifier = Modifier.weight(1f))
//
//                        Text(
//                            text = "Designed with ❤️ by Aaron Lam Kong Yew (33521808)",
//                            style = TextStyle(fontSize = 12.sp),
//                        )
//
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }
//                }
            }
        }
    }
}