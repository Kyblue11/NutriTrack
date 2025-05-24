package com.aaronlamkongyew33521808.myapplication.ui.charts

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.aaronlamkongyew33521808.myapplication.viewmodel.StatsViewModel

@Composable
fun TotalScoreChartScreen(viewModel: StatsViewModel) {
    val data = viewModel.allUsersTotalScore.observeAsState(emptyList()).value
    ChartBar(data)
}