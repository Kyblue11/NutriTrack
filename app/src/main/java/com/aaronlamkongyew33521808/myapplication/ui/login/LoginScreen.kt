package com.aaronlamkongyew33521808.myapplication.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aaronlamkongyew33521808.myapplication.R
import com.aaronlamkongyew33521808.myapplication.auth.AuthManager
import com.aaronlamkongyew33521808.myapplication.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit,
    onRegister: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val context = LocalContext.current
    val userIds by viewModel.userIds.collectAsStateWithLifecycle()
    val result by viewModel.loginResult.collectAsStateWithLifecycle()

    var expanded by remember { mutableStateOf(false) }
    var selectedUserId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    LaunchedEffect(result) {
        when (result) {
            true -> {
                Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show()
                AuthManager.login(selectedUserId, context)
                onLoginSuccess(selectedUserId)
            }
            false -> {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                viewModel.resetLoginResult()
            }
            null -> { /* Do nothing */ }
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Log in", fontSize = (screenWidth * 0.05).sp) }
        )
    }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding((screenWidth * 0.04).dp),
            verticalArrangement = Arrangement.spacedBy((screenHeight * 0.02).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedUserId,
                    onValueChange = { /* no-op */ },
                    readOnly = true,
                    label = { Text("My ID (Provided by your Clinician)", fontSize = (screenWidth * 0.04).sp) },
                    placeholder = { Text("Select ID", fontSize = (screenWidth * 0.035).sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    userIds.forEach { id ->
                        DropdownMenuItem(
                            text = { Text(id, fontSize = (screenWidth * 0.04).sp) },
                            onClick = {
                                selectedUserId = id
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", fontSize = (screenWidth * 0.04).sp) },
                placeholder = { Text("Enter your password", fontSize = (screenWidth * 0.035).sp) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.eye_open else R.drawable.eye_close),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height((screenHeight * 0.02).dp))

            Text(
                text = """
                    This app is only for pre-registered users. Please have your ID and phone number ready before continuing.
                """.trimIndent(),
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodySmall.copy(
                    lineHeight = (screenWidth * 0.05).sp
                ),
                fontSize = (screenWidth * 0.035).sp
            )
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                thickness = (screenHeight * 0.001).dp,
                modifier = Modifier.padding(vertical = (screenHeight * 0.01).dp)
            )

            Button(
                onClick = { viewModel.login(selectedUserId, password) },
                enabled = selectedUserId.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue", fontSize = (screenWidth * 0.045).sp)
            }

            TextButton(onClick = onRegister) {
                Text("Don't have an account? Register", fontSize = (screenWidth * 0.04).sp)
            }
        }
    }
}