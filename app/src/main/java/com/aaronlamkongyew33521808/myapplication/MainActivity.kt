package com.aaronlamkongyew33521808.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aaronlamkongyew33521808.myapplication.ui.navigation.AppNavGraph
import com.aaronlamkongyew33521808.myapplication.ui.theme.NutriTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriTrackTheme {
                AppNavGraph()
            }
        }
    }
}