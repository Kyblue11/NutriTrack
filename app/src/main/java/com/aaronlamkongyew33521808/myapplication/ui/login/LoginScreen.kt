package com.aaronlamkongyew33521808.myapplication.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aaronlamkongyew33521808.myapplication.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit,
    onRegister: () -> Unit
) {
    val loginResult by viewModel.loginSuccess.collectAsState()

    var userId by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    LaunchedEffect(loginResult) {
        if (loginResult == true) {
            onLoginSuccess(userId)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Log In") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("User ID") },
                placeholder = { Text("e.g. 012345") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                placeholder = { Text("Enter number") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.login(userId, phone) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }

            TextButton(onClick = onRegister) {
                Text("Don't have an account? Register")
            }

            loginResult?.let {
                if (!it) {
                    Text(
                        "Invalid credentials!",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}