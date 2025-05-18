package com.aaronlamkongyew33521808.myapplication.ui.charts

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.aaronlamkongyew33521808.myapplication.viewmodel.StatsViewModel

@Composable
fun SubscoreScatterScreen(viewModel: StatsViewModel) {
    val data = viewModel.allUsersSubScores.observeAsState(emptyList()).value
    ChartScatter(data.map { it.fruitScore to it.vegetableScore })
}