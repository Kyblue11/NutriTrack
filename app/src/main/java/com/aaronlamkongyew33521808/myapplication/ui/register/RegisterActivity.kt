package com.aaronlamkongyew33521808.myapplication.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.aaronlamkongyew33521808.myapplication.viewmodel.RegisterViewModel
import com.aaronlamkongyew33521808.myapplication.ui.login.LoginActivity

class RegisterActivity : ComponentActivity() {
    private val vm by viewModels<RegisterViewModel>()

    class RegisterActivity : ComponentActivity() {
        private val vm by viewModels<RegisterViewModel>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                RegisterScreen(
                    viewModel = vm,
                    onRegisterSuccess = { userId ->
                        // after registration go back to Login
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("userId", userId)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}