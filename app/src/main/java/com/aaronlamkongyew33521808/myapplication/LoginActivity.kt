package com.aaronlamkongyew33521808.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aaronlamkongyew33521808.myapplication.ui.theme.NutriTrackTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class LoginActivity : ComponentActivity() {
    // store  parsed CSV data
    private lateinit var userData: List<UserData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        userData = loadCSV(this)
        setContent {
            NutriTrackTheme {
                LoginScreen(userData)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(userData: List<UserData>) {

    var selectedUserId by remember { mutableStateOf(userData.firstOrNull()?.userId ?: "") }
    var phoneNumber by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Needed for Toast
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Log in",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 40.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedUserId,
                    onValueChange = { /* readOnly, so do nothing */ },
                    label = { Text("My ID (Provided by your Clinician)") },
                    placeholder = { Text("e.g. 012345") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    userData.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user.userId) },
                            onClick = {
                                selectedUserId = user.userId
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone number") },
                placeholder = { Text("Enter your number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = """
                    This app is only for pre-registered users. Please have your ID and phone number handy before continuing.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val matchedUser = userData.find {
                        it.userId == selectedUserId && it.phoneNumber == phoneNumber
                    }
                    if (matchedUser != null) {
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()

                        val intent = Intent(context, DashboardActivity::class.java).apply {
                            putExtra("userId", matchedUser.userId)
                        }
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Invalid credentials!", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

fun loadCSV(context: Context): List<UserData> {
    val userList = mutableListOf<UserData>()

    try {
        val inputStream = context.assets.open("participantData.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            val allLines = lines.toList()
            if (allLines.isNotEmpty()) {
                allLines.drop(1).forEach { line ->
                    val tokens = line.split(",")
                    userList.add(
                        UserData(
                            phoneNumber = tokens[0].trim().removePrefix("\""),
                            userId = tokens[1].trim(),
                            sex = tokens[2].trim(),
                            HEIFAtotalscoreMale = tokens[3].toDoubleOrNull() ?: 0.0,
                            HEIFAtotalscoreFemale = tokens[4].toDoubleOrNull() ?: 0.0,
                            discretionaryHEIFAscoreMale = tokens[5].toDoubleOrNull() ?: 0.0,
                            discretionaryHEIFAscoreFemale = tokens[6].toDoubleOrNull() ?: 0.0,
                            vegetablesHEIFAscoreMale = tokens[8].toDoubleOrNull() ?: 0.0,
                            vegetablesHEIFAscoreFemale = tokens[9].toDoubleOrNull() ?: 0.0,
                            fruitHEIFAscoreMale = tokens[19].toDoubleOrNull() ?: 0.0,
                            fruitHEIFAscoreFemale = tokens[20].toDoubleOrNull() ?: 0.0,
                            grainsAndCerealsHEIFAscoreMale = tokens[29].toDoubleOrNull() ?: 0.0,
                            grainsAndCerealsHEIFAscoreFemale = tokens[30].toDoubleOrNull() ?: 0.0,
                            wholegrainsHEIFAscoreMale = tokens[33].toDoubleOrNull() ?: 0.0,
                            wholegrainsHEIFAscoreFemale = tokens[34].toDoubleOrNull() ?: 0.0,
                            meatAndAlternativesHEIFAscoreMale = tokens[36].toDoubleOrNull() ?: 0.0,
                            meatAndAlternativesHEIFAscoreFemale = tokens[37].toDoubleOrNull() ?: 0.0,
                            dairyAndAlternativesHEIFAscoreMale = tokens[40].toDoubleOrNull() ?: 0.0,
                            dairyAndAlternativesHEIFAscoreFemale = tokens[41].toDoubleOrNull() ?: 0.0,
                            sodiumHEIFAscoreMale = tokens[43].toDoubleOrNull() ?: 0.0,
                            sodiumHEIFAscoreFemale = tokens[44].toDoubleOrNull() ?: 0.0,
                            alcoholHEIFAscoreMale = tokens[46].toDoubleOrNull() ?: 0.0,
                            alcoholHEIFAscoreFemale = tokens[47].toDoubleOrNull() ?: 0.0,
                            waterHEIFAscoreMale = tokens[49].toDoubleOrNull() ?: 0.0,
                            waterHEIFAscoreFemale = tokens[50].toDoubleOrNull() ?: 0.0,
                            sugarHEIFAscoreMale = tokens[54].toDoubleOrNull() ?: 0.0,
                            sugarHEIFAscoreFemale = tokens[55].toDoubleOrNull() ?: 0.0,
                            unsaturatedFatHEIFAscoreMale = tokens[60].toDoubleOrNull() ?: 0.0,
                            unsaturatedFatHEIFAscoreFemale = tokens[61].toDoubleOrNull() ?: 0.0
                        )
                    )
                }
            }
        }
    } catch (e: Exception) {
        Log.e("CSV_DEBUG", "Error reading CSV from assets: ", e)
    }
    return userList
}

data class UserData(
    val phoneNumber: String,
    val userId: String,
    val sex : String,
    val HEIFAtotalscoreMale: Double,
    val HEIFAtotalscoreFemale: Double,
    val discretionaryHEIFAscoreMale: Double,
    val discretionaryHEIFAscoreFemale: Double,
    val vegetablesHEIFAscoreMale: Double,
    val vegetablesHEIFAscoreFemale: Double,
    val fruitHEIFAscoreMale: Double,
    val fruitHEIFAscoreFemale: Double,
    val grainsAndCerealsHEIFAscoreMale: Double,
    val grainsAndCerealsHEIFAscoreFemale: Double,
    val wholegrainsHEIFAscoreMale: Double,
    val wholegrainsHEIFAscoreFemale: Double,
    val meatAndAlternativesHEIFAscoreMale: Double,
    val meatAndAlternativesHEIFAscoreFemale: Double,
    val dairyAndAlternativesHEIFAscoreMale: Double,
    val dairyAndAlternativesHEIFAscoreFemale: Double,
    val sodiumHEIFAscoreMale: Double,
    val sodiumHEIFAscoreFemale: Double,
    val alcoholHEIFAscoreMale: Double,
    val alcoholHEIFAscoreFemale: Double,
    val waterHEIFAscoreMale: Double,
    val waterHEIFAscoreFemale: Double,
    val sugarHEIFAscoreMale: Double,
    val sugarHEIFAscoreFemale: Double,
    val unsaturatedFatHEIFAscoreMale: Double,
    val unsaturatedFatHEIFAscoreFemale: Double,
)