package com.aaronlamkongyew33521808.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aaronlamkongyew33521808.myapplication.auth.AuthManager
import com.aaronlamkongyew33521808.myapplication.ui.navigation.AppNavGraph
import com.aaronlamkongyew33521808.myapplication.ui.theme.NutriTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // restore from prefs if there was a logged‐in user
        val prefs    = getSharedPreferences("NutriTrackPrefs", MODE_PRIVATE)
        val isLogged = prefs.getBoolean("isLoggedIn", false)
        val lastUser = prefs.getString("lastUserId", null)
        if (isLogged && lastUser != null) {
            AuthManager._userId.value = lastUser
        }

        setContent {
            NutriTrackTheme {
                AppNavGraph()
            }
        }
    }
}
