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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onLogout: () -> Unit,
    onClinician: () -> Unit,
    navController: NavHostController
) {
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
                        fontSize = 20.sp
                    )
                },
            )
        },
        bottomBar = {
            BottomBar(navController, userId)
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ACCOUNT", style = MaterialTheme.typography.labelLarge)
                IconButton(onClick = {
                    if (!editMode) {
                        // Enter edit mode
                        editMode = true
                    } else {
                        // Attempt to save
                        coroutineScope.launch {
                            // Validate newPass == confirmPass first
                            if (newPass != confirmPass) {
                                Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show()
                                return@launch
                            }
                            val success = viewModel.updateProfile( // TODO: make it so that editing name/phone only doesn't require password
                                currentPass = currentPass,
                                newName     = newName,
                                newPhone    = newPhone,
                                newPass     = newPass
                            )
                            if (success) {
                                Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                                editMode = false
                            } else {
                                Toast.makeText(context, "Update failed: incorrect password", Toast.LENGTH_SHORT).show()
                            }
                            // Clear password fields
                            currentPass = ""
                            newPass     = ""
                            confirmPass = ""
                        }
                    }
                }) {
                    Icon(
                        imageVector   = if (editMode) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = if (editMode) "Save" else "Edit"
                    )
                }
            }

            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                if (!editMode) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(name, style = MaterialTheme.typography.titleMedium)
                        Text(phone, style = MaterialTheme.typography.bodyMedium)
                        Text("ID: $userId", style = MaterialTheme.typography.bodySmall)
                    }
                } else {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Name") }
                        )
                        OutlinedTextField(
                            value = newPhone,
                            onValueChange = { newPhone = it },
                            label = { Text("Phone") }
                        )
                        OutlinedTextField(
                            value = currentPass,
                            onValueChange = { currentPass = it },
                            label = { Text("Current Password") },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        OutlinedTextField(
                            value = newPass,
                            onValueChange = { newPass = it },
                            label = { Text("New Password") },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        OutlinedTextField(
                            value = confirmPass,
                            onValueChange = { confirmPass = it },
                            label = { Text("Confirm Password") },
                            visualTransformation = PasswordVisualTransformation()
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("OTHER SETTINGS", style = MaterialTheme.typography.labelLarge)

            SettingsRow(label = "Logout", onClick = onLogout)
            SettingsRow(label = "Clinician Login",  onClick = { showClinicianDialog = true })

            if (showClinicianDialog) {
                AlertDialog(
                    onDismissRequest = { showClinicianDialog = false },
                    title = { Text("Clinician Login") },
                    text = {
                        Column {
                            OutlinedTextField(
                                label = { Text("Clinician Key") },
                                placeholder = { Text("Enter your clinician key") },
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
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showClinicianDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
@Composable
private fun SettingsRow(label: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Icon(Icons.Default.ArrowForward, contentDescription = null)
    }
}