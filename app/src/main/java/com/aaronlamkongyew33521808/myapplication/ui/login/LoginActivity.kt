package com.aaronlamkongyew33521808.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.aaronlamkongyew33521808.myapplication.DashboardActivity
import com.aaronlamkongyew33521808.myapplication.ui.register.RegisterActivity
import com.aaronlamkongyew33521808.myapplication.viewmodel.LoginViewModel

class LoginActivity : ComponentActivity() {
    private val vm by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(
                viewModel = vm,
                onLoginSuccess = { userId ->
                    // navigate to Dashboard
                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    finish()
                },
                onRegister = {
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
            )
        }
    }
}