package com.aaronlamkongyew33521808.myapplication.ui.clinician

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.data.dao.UserDao
import com.aaronlamkongyew33521808.myapplication.viewmodel.ClinicianViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicianDashboardScreen(
    onDone: () -> Unit,
    dao: UserDao = AppDatabase.getDatabase(LocalContext.current).userDao(),
    viewModel: ClinicianViewModel = viewModel(factory = ClinicianViewModel.Factory(dao))
) {
    val avgMale   by viewModel.avgMale.collectAsState(initial = 0.0)
    val avgFemale by viewModel.avgFemale.collectAsState(initial = 0.0)
    val insights  by viewModel.insights.collectAsState(initial = emptyList())
    val loading   by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAverages()
        viewModel.loadAveragesForFats()
        viewModel.loadAveragesForHealthyFoods()
    }

    Scaffold(
        topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Clinician Dashboard",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
        )
    },) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Average HEIFA (Male): ${"%.1f".format(avgMale)}")
            Text("Average HEIFA (Female): ${"%.1f".format(avgFemale)}")
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.findPatterns() },
                enabled = !loading
            ) { Text(if (loading) "Thinking…" else "Find Data Patterns") }

            insights.forEach { pattern ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(pattern, Modifier.padding(8.dp))
                }
            }

            Spacer(Modifier.weight(1f))
            Button(onClick = onDone, Modifier.align(Alignment.End)) {
                Text("Done")
            }
        }
    }
}
