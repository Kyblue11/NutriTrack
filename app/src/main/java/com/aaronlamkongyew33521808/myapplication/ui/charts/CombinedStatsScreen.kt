package com.aaronlamkongyew33521808.myapplication.ui.charts

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaronlamkongyew33521808.myapplication.viewmodel.StatsViewModel
import androidx.compose.ui.Alignment
import com.aaronlamkongyew33521808.myapplication.viewmodel.ClinicianKeyViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.InsightsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinedStatsScreen(
    viewModel: StatsViewModel,
    keyVm: ClinicianKeyViewModel,
    insightsViewModel: InsightsViewModel,
    onMenuClick: () -> Unit,
    userId: String
) {
    val context = LocalContext.current

    LaunchedEffect(userId) { insightsViewModel.load(userId) }
    val mySubs    by insightsViewModel.subScores.collectAsState()
    val passkey by keyVm.key.collectAsState()


    var showDialog by rememberSaveable { mutableStateOf(false) }
    var enteredKey by rememberSaveable { mutableStateOf("") }
    var showAdvanced by rememberSaveable { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "User Statistics",
                        fontWeight = FontWeight.Bold,
                        fontSize = (screenWidth * 0.05).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onMenuClick()
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "DrawerMenu"
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding((screenWidth * 0.04).dp),
        ) {
            ChartPie(
                data = mySubs,
                chartTitle = "My HEIFA Sub‑score Breakdown"
            )

            TextButton(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Show all users", )
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title   = { Text("Clinician Login") },
                    text    = {
                        OutlinedTextField(
                            value = enteredKey,
                            onValueChange = { enteredKey = it },
                            label = { Text("Enter passphrase") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation()
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (enteredKey == passkey) {
                                showAdvanced = true
                            } else {
                                Toast.makeText(context, "Wrong passphrase", Toast.LENGTH_SHORT).show()
                            }
                            enteredKey = ""
                            showDialog = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDialog = false
                            enteredKey = ""
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // conditionally render the two full charts
            if (showAdvanced) {

                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                Box(Modifier
                    .fillMaxWidth()
                    .height((screenHeight * 0.4).dp)
                ) {
                    TotalScoreChartScreen(viewModel = viewModel)
                }
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                Box(Modifier
                    .fillMaxWidth()
                    .height((screenHeight * 0.4).dp)
                ) {
                    SubscoreScatterScreen(viewModel = viewModel)
                }
            }
        }
    }
}