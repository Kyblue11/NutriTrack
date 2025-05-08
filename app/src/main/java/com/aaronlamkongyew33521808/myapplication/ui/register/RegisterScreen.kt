package com.aaronlamkongyew33521808.myapplication.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity
import com.aaronlamkongyew33521808.myapplication.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: (String) -> Unit
) {
    val registerResult by viewModel.registerSuccess.collectAsState()

    var userId by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var sexExpanded by remember { mutableStateOf(false) }
    var sex by remember { mutableStateOf("") }
    val sexOptions = listOf("Male", "Female")

    LaunchedEffect(registerResult) {
        if (registerResult == true) {
            onRegisterSuccess(userId)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Register New Account") }) }
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

            ExposedDropdownMenuBox(
                expanded = sexExpanded,
                onExpandedChange = { sexExpanded = !sexExpanded }
            ) {
                OutlinedTextField(
                    value = sex,
                    onValueChange = {},
                    label = { Text("Sex") },
                    placeholder = { Text("Select sex") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(sexExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = sexExpanded,
                    onDismissRequest = { sexExpanded = false }
                ) {
                    sexOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                sex = option
                                sexExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.register(
                        UserEntity(
                            userId = userId,
                            phoneNumber = phone,
                            sex = sex,
                            HEIFAtotalscoreMale = 0.0,
                            HEIFAtotalscoreFemale = 0.0
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Account")
            }

            registerResult?.let {
                if (!it) {
                    Text(
                        "Registration failed! Check inputs.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
