package com.aaronlamkongyew33521808.myapplication.ui.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aaronlamkongyew33521808.myapplication.viewmodel.RegisterViewModel

import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onDone: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val context = LocalContext.current
    val userIds by viewModel.userIds.collectAsStateWithLifecycle()
    val canClaim by viewModel.canClaim.collectAsStateWithLifecycle()
    val result by viewModel.claimResult.collectAsStateWithLifecycle()

    var userId by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }

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

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Register Account", fontSize = (screenWidth * 0.05).sp) }
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
            if (canClaim != true) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded; triedVerify = false; viewModel.resetCanClaim() }
                ) {
                    OutlinedTextField(
                        value = userId,
                        onValueChange = {},
                        label = { Text("My ID (Provided by your Clinician)", fontSize = (screenWidth * 0.04).sp) },
                        placeholder = { Text("Select pre-registered ID", fontSize = (screenWidth * 0.035).sp) },
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
                                text = { Text(id, fontSize = (screenWidth * 0.04).sp) },
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
                    onValueChange = { phone = it; viewModel.resetCanClaim() }, // Band-aid fix to ensure to reset register attempt, but will be incurred for every key-stroke
                    singleLine = true,
                    label = { Text("Phone Number", fontSize = (screenWidth * 0.04).sp) },
                    placeholder = { Text("Enter your registered number", fontSize = (screenWidth * 0.035).sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height((screenHeight * 0.03).dp))

                Button(
                    onClick = {
                        triedVerify = true
                        viewModel.verifyIdPhone(userId, phone)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = userId.isNotBlank() && phone.isNotBlank()
                ) {
                    Text("Next", fontSize = (screenWidth * 0.045).sp)
                }

                if (triedVerify && canClaim == false) {
                    Text(
                        "No pre‑registered user found",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = (screenWidth * 0.04).sp
                    )
                }
            } else {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Enter your user name", fontSize = (screenWidth * 0.04).sp) },
                    placeholder = { Text("e.g. John Doe", fontSize = (screenWidth * 0.035).sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Password", fontSize = (screenWidth * 0.04).sp) },
                    placeholder = { Text("Enter a new password", fontSize = (screenWidth * 0.035).sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                OutlinedTextField(
                    value = confirm,
                    onValueChange = { confirm = it },
                    label = { Text("Confirm Password", fontSize = (screenWidth * 0.04).sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )

                Text(
                    text = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = (screenWidth * 0.05).sp
                    ),
                    fontSize = (screenWidth * 0.035).sp,
                    modifier = Modifier.padding((screenWidth * 0.02).dp),
                    color = if (isPasswordSecure(pass)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height((screenHeight * 0.03).dp))

                Button(
                    onClick = { viewModel.claimAccount(userId, phone, name, pass) },
                    enabled = (pass.isNotBlank()) && (pass == confirm)
                            && (name.isNotBlank()) && (isPasswordSecure(pass)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register", fontSize = (screenWidth * 0.045).sp)
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