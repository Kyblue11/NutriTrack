package com.aaronlamkongyew33521808.myapplication.ui.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aaronlamkongyew33521808.myapplication.viewmodel.SettingsViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import com.aaronlamkongyew33521808.myapplication.ui.navigation.BottomBar
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.aaronlamkongyew33521808.myapplication.auth.AuthManager
import kotlinx.coroutines.launch

import  com.aaronlamkongyew33521808.myapplication.ui.register.isPasswordSecure

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onLogout: () -> Unit,
    onClinician: () -> Unit,
    navController: NavHostController,
    onReturnHome: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val name by viewModel.name.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val userId by viewModel.id.collectAsState()
    var editMode by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(name) }
    var newPhone by remember { mutableStateOf(phone) }
    var currentPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var showClinicianDialog by remember { mutableStateOf(false) }
    var passphrase by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = (screenWidth * 0.05).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onReturnHome()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
//                modifier = Modifier.height((screenHeight * 0.1).dp)
            )
        },
        bottomBar = {
            BottomBar(navController, userId, screenWidth, screenHeight)
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding((screenWidth * 0.04).dp),
            verticalArrangement = Arrangement.spacedBy((screenHeight * 0.03).dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ACCOUNT", style = MaterialTheme.typography.labelLarge, fontSize = (screenWidth * 0.045).sp)
                IconButton(onClick = {
                    if (!editMode) {
                        newName = name
                        newPhone = phone
                        // Enter edit mode
                        editMode = true
                    } else {
                        // Attempt to save
                        coroutineScope.launch {
                            if (currentPass.isEmpty() && newPass.isEmpty() && confirmPass.isEmpty()) {
                                // Update only name and phone
                                val success = viewModel.updateProfile(
                                    newName = newName.ifBlank { name },
                                    newPhone = newPhone.ifBlank { phone }
                                )
                                if (success) {
                                    Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                                    editMode = false
                                } else {
                                    Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                // Validate password fields
                                if (newPass != confirmPass) {
                                    Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show()
                                    return@launch
                                }
                                // new password is empty, keep old password
                                if (newPass.isEmpty() || !isPasswordSecure(newPass)) {
                                    Toast.makeText(context, "New password is invalid or insecure!", Toast.LENGTH_SHORT).show()
                                    return@launch
                                }
                                val success = viewModel.updateProfile(
                                    currentPass = currentPass,
                                    newName = newName.ifBlank { name },
                                    newPhone = newPhone.ifBlank { phone },
                                    newPass = newPass
                                )
                                if (success) {
                                    Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                                    editMode = false
                                } else {
                                    Toast.makeText(context, "Update failed: incorrect current password", Toast.LENGTH_SHORT).show()
                                }
                            }
                            // Clear password fields
                            currentPass = ""
                            newPass = ""
                            confirmPass = ""
                        }
                    }
                }) {
                    Icon(
                        imageVector = if (editMode) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = if (editMode) "Save" else "Edit"
                    )
                }
            }

            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = (screenHeight * 0.01).dp),
                shape = RoundedCornerShape((screenWidth * 0.02).dp),
                elevation = CardDefaults.cardElevation(defaultElevation = (screenHeight * 0.002).dp)
            ) {
                if (!editMode) {
                    Column(
                        Modifier.padding((screenWidth * 0.04).dp),
                        verticalArrangement = Arrangement.spacedBy((screenHeight * 0.005).dp)
                    ) {
                        Text(name, style = MaterialTheme.typography.titleMedium, fontSize = (screenWidth * 0.045).sp)
                        Text(phone, style = MaterialTheme.typography.bodyMedium, fontSize = (screenWidth * 0.04).sp)
                        Text("ID: $userId", style = MaterialTheme.typography.bodySmall, fontSize = (screenWidth * 0.035).sp)
                    }
                } else {
                    Column(
                        Modifier.padding((screenWidth * 0.04).dp),
                        verticalArrangement = Arrangement.spacedBy((screenHeight * 0.01).dp)
                    ) {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Name", fontSize = (screenWidth * 0.04).sp) }
                        )
                        OutlinedTextField(
                            value = newPhone,
                            onValueChange = { newPhone = it },
                            label = { Text("Phone", fontSize = (screenWidth * 0.04).sp) }
                        )
                        Spacer(Modifier.height((screenHeight * 0.005).dp))
                        Divider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            thickness = (screenHeight * 0.001).dp
                        )
                        Spacer(Modifier.height((screenHeight * 0.005).dp))
                        OutlinedTextField(
                            value = currentPass,
                            onValueChange = { currentPass = it },
                            label = { Text("Current Password", fontSize = (screenWidth * 0.04).sp) },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        OutlinedTextField(
                            value = newPass,
                            onValueChange = { newPass = it },
                            label = { Text("New Password", fontSize = (screenWidth * 0.04).sp) },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        OutlinedTextField(
                            value = confirmPass,
                            onValueChange = { confirmPass = it },
                            label = { Text("Confirm Password", fontSize = (screenWidth * 0.04).sp) },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Spacer(Modifier.height((screenHeight * 0.005).dp))
                        Text(
                            text = """
                                If you do not wish to change your password, leave the 3 password fields blank.
                                If you wish to change your password, please enter your current password and the new password twice.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                lineHeight = (screenWidth * 0.05).sp
                            ),
                            fontSize = (screenWidth * 0.035).sp,
                            textAlign = TextAlign.Justify,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(Modifier.height((screenHeight * 0.02).dp))
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                thickness = (screenHeight * 0.001).dp,
                modifier = Modifier.padding(vertical = (screenHeight * 0.01).dp)
            )
            Spacer(Modifier.height((screenHeight * 0.01).dp))
            Text("OTHER SETTINGS", style = MaterialTheme.typography.labelLarge, fontSize = (screenWidth * 0.045).sp)

            SettingsRow(label = "Logout", onClick = {
                AuthManager.logout(context)
                onLogout()
            })
            SettingsRow(label = "Clinician Login", onClick = { showClinicianDialog = true })

            if (showClinicianDialog) {
                AlertDialog(
                    onDismissRequest = { showClinicianDialog = false },
                    title = { Text("Clinician Login", fontSize = (screenWidth * 0.045).sp) },
                    text = {
                        Column {
                            OutlinedTextField(
                                label = { Text("Clinician Key", fontSize = (screenWidth * 0.04).sp) },
                                placeholder = { Text("Enter your clinician key", fontSize = (screenWidth * 0.035).sp) },
                                value = passphrase,
                                onValueChange = { passphrase = it },
                                visualTransformation = PasswordVisualTransformation()
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (passphrase == "dollar-entry-apples") {
                                onClinician()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Wrong passphrase",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            showClinicianDialog = false
                        }) {
                            Text("OK", fontSize = (screenWidth * 0.04).sp)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showClinicianDialog = false }) {
                            Text("Cancel", fontSize = (screenWidth * 0.04).sp)
                        }
                    }
                )
            }
        }
    }
}
@Composable
private fun SettingsRow(label: String, onClick: () -> Unit) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = (screenHeight * 0.015).dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Icon(Icons.Default.ArrowForward, contentDescription = null)
    }
}