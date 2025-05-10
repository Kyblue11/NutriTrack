package com.aaronlamkongyew33521808.myapplication.ui.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aaronlamkongyew33521808.myapplication.viewmodel.RegisterViewModel

import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val userIds  by viewModel.userIds.collectAsStateWithLifecycle()
    val canClaim by viewModel.canClaim.collectAsStateWithLifecycle()
    val result   by viewModel.claimResult.collectAsStateWithLifecycle()

    var userId  by remember { mutableStateOf("") }
    var phone   by remember { mutableStateOf("") }
    var name    by remember { mutableStateOf("") }
    var pass    by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    // track whether they've attempted verify
    var triedVerify by remember { mutableStateOf(false) }

    // for dropdown menu
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        if (result == true) {
            Toast.makeText(context, "Registration Successful!", Toast.LENGTH_LONG).show()
            onDone()
        }
    }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Register Account") }) }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (canClaim != true) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded; triedVerify = false; viewModel.resetCanClaim() }

                ) {
                    OutlinedTextField(
                        value = userId,
                        onValueChange = {},
                        label = { Text("My ID (Provided by your Clinician)") },
                        placeholder = { Text("Select pre-registered ID") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false;  }
                    ) {
                        userIds.forEach { id ->
                            DropdownMenuItem(
                                text = { Text(id) },
                                onClick = {
                                    userId = id
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; viewModel.resetCanClaim() }, // TODO: bad practice, but this makes it work!!! Function is called for every input?!
                    singleLine = true,
                    label = { Text("Phone Number") },
                    placeholder = { Text("Enter your registered number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        triedVerify = true
                        viewModel.verifyIdPhone(userId, phone)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = userId.isNotBlank() && phone.isNotBlank()
                ) {
                    Text("Next")
                }

                if (triedVerify && canClaim == false) {
                    Text(
                        "No pre‑registered user found",
                        color = MaterialTheme.colorScheme.error
                    );

                }
            } else {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Enter your user name") },
                    placeholder = { Text("e.g. John Doe") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Password") },
                    placeholder = { Text(
                        "Enter a new password",
                    ) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirm,
                    onValueChange = { confirm = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp),
                    color = if (isPasswordSecure(pass)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.claimAccount(userId, phone, name, pass) },
                    enabled = (pass.isNotBlank()) && (pass == confirm)
                            && (name.isNotBlank()) && (isPasswordSecure(pass)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        }
    }
}

fun isPasswordSecure(password: String): Boolean {
    val passwordPattern = Pattern.compile(
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
    )
    return passwordPattern.matcher(password).matches()
}