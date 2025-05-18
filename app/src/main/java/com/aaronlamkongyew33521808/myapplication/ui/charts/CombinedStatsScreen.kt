package com.aaronlamkongyew33521808.myapplication.ui.charts

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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaronlamkongyew33521808.myapplication.viewmodel.StatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinedStatsScreen(
    viewModel: StatsViewModel,
    onMenuClick: () -> Unit,
) {
    // observe both data streams
    val totalData   by viewModel.allUsersTotalScore.observeAsState(emptyList())
    val subData     by viewModel.allUsersSubScores    .observeAsState(emptyList())
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
    Column(Modifier
        .padding(padding)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding((screenWidth * 0.04).dp),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height((screenHeight * 0.5).dp)
        ) {
            TotalScoreChartScreen(viewModel = viewModel)
        }

        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

        Box(
            Modifier
                .fillMaxWidth()
                .height((screenHeight * 0.5).dp)
        ) {
            SubscoreScatterScreen(viewModel = viewModel)
        }
    }
}
}