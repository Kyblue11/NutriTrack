package com.aaronlamkongyew33521808.myapplication.ui.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.aaronlamkongyew33521808.myapplication.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit,
    onRegister: () -> Unit
) {
    val context = LocalContext.current
    val userIds by viewModel.userIds.collectAsStateWithLifecycle()
    val loginResult by viewModel.loginSuccess.collectAsStateWithLifecycle()

    // UI state
    var expanded by remember { mutableStateOf(false) }
    var selectedUserId by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var phoneVisible by remember { mutableStateOf(false) }

    LaunchedEffect(userIds) {
        Log.d("LoginVM", "userIds changed → $userIds")
    }

    // show toasts & navigate
    LaunchedEffect(loginResult) {
        if (loginResult == true) {

            Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
            onLoginSuccess(selectedUserId)
        }
            else {
                Toast.makeText(context, "Invalid credentials!", Toast.LENGTH_LONG).show()
            }
        }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Log in") }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth(),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedUserId,
                    onValueChange = { /*no-op*/ },
                    readOnly = true,
                    label = { Text("My ID (Provided by your Clinician)") },
                    placeholder = { Text("Select ID") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    userIds.forEach { id ->
                        DropdownMenuItem(
                            text = { Text(id) },
                            onClick = {
                                selectedUserId = id
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                placeholder = { Text("Enter number") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = if (phoneVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { phoneVisible = !phoneVisible }) {
                        Icon(
                            painter = painterResource(id = if (phoneVisible) R.drawable.eye_open else R.drawable.eye_close),
                            contentDescription = if (phoneVisible) "Hide phone" else "Show phone"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text =
                    """
                        This app is only for pre-registered users. Please have your ID and phone number ready before continuing.
                    """.trimIndent(),
                textAlign = TextAlign.Justify,
                style = TextStyle(fontSize = 16.sp),
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.login(selectedUserId, phone) },
                enabled = selectedUserId.isNotBlank() && phone.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }

            TextButton(onClick = onRegister) {
                Text("Don't have an account? Register")
            }
        }
    }
}