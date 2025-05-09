package com.aaronlamkongyew33521808.myapplication.ui.insights

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.InsightsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    userId: String,
    vm: InsightsViewModel = viewModel()
) {
    // kick off load
    LaunchedEffect(userId) { vm.load(userId) }

    val total   by vm.totalScore.collectAsState()
    val subs    by vm.subScores.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Insights: Food Score") })
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            Text("Sub‐scores", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
            LazyColumn(Modifier.weight(1f)) {
                items(subs.toList()) { (label, score) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(label)
                        Text("${"%.1f".format(score)}")
                    }
                }
            }
            Divider()
            Text("Total Score: ${"%.2f".format(total)}/100",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp))
        }
    }
}