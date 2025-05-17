package com.aaronlamkongyew33521808.myapplication.ui.insights

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.aaronlamkongyew33521808.myapplication.R
import com.aaronlamkongyew33521808.myapplication.ui.navigation.BottomBar
import com.aaronlamkongyew33521808.myapplication.viewmodel.InsightsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    userId: String,
    vm: InsightsViewModel = viewModel(),
    navController: NavHostController,
    onReturnHome : () -> Unit,
    onImproveClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    // kick off load
    LaunchedEffect(userId) { vm.load(userId) }

    val total   by vm.totalScore.collectAsState()
    val subs    by vm.subScores.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Insights: Food Score",
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
        bottomBar = { BottomBar(navController, userId, screenWidth, screenHeight) }

    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
                InsightsContent(
                    mainTotalScore = total,
                    subScores = subs,
                    onShareClick = {
                        val textToShare = "My Food Quality Score is ${"%.2f".format(total)}/100. " + "Check out my other scores!"
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, textToShare)
                        }
                        context.startActivity(
                            Intent.createChooser(shareIntent, "Share via")
                        )                    },
                    onImproveClick = {
                        onImproveClick()
                    }
                )
            }
        }
    }


//---------------------------------------------------------------------------
// Composables
//---------------------------------------------------------------------------

@Composable
fun InsightsContent(
    mainTotalScore: Double,
    subScores: Map<String, Double>,
    onShareClick: () -> Unit,
    onImproveClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding((screenWidth * 0.04).dp)
    ) {

        subScores.forEach { (label, value) ->
            ScoreRowWithSlider(
                category = label,
                score = value
            )
            Spacer(modifier = Modifier.height((screenHeight * 0.005).dp))
        }

        Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
        Divider()
        Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))

        Text(
            text = "Total Food Quality Score",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            fontSize = (screenWidth * 0.045).sp
        )

        // Use the same custom slider—this time with the special total settings.
        ScoreRowWithSlider(
            category = "Total",
            score = mainTotalScore,
            isTotalScore = true
        )

        // Display the total score value below the slider.
        Text(
            text = "${"%.2f".format(mainTotalScore)}/100",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary),
            fontSize = (screenWidth * 0.05).sp
        )

        Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
        Button(
            onClick = onShareClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.share),
                contentDescription = "Share",
                modifier = Modifier.size((screenWidth * 0.05).dp)
            )
            Spacer(modifier = Modifier.width((screenWidth * 0.02).dp))
            Text("Share with someone", fontSize = (screenWidth * 0.03).sp)
        }

        Spacer(modifier = Modifier.height((screenHeight * 0.02).dp))
        Button(
            onClick = onImproveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.coach),
                contentDescription = "Improve",
                modifier = Modifier.size((screenWidth * 0.05).dp)
            )
            Spacer(modifier = Modifier.width((screenWidth * 0.01).dp))
            Text("Improve my diet!", fontSize = (screenWidth * 0.03).sp)
        }
    }
}

// Disclaimer:
// GenAi model ChatGPT 4.o was used to format and fix alignment of
// this ScoreRowWithSlider composable, as well as performing calculations for
// offsetY, activeWidth, stepSpacing and cornerRadius
@Composable
fun ScoreRowWithSlider(
    category: String,
    score: Double,
    isTotalScore: Boolean = false
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    // Determine maximum value and fraction.
    val maxScore = if (isTotalScore) {
        100.0
    } else {
        val fivePointCategories = listOf(
            "Grains & Cereals", "Whole Grains", "Water", "Alcohol", "Saturated Fats", "Unsaturated Fats"
        )
        if (category in fivePointCategories) 5.0 else 10.0
    }
    val fraction = (score / maxScore).coerceIn(0.0, 1.0).toFloat()

    // Define dimensions based on whether this is the total score.
    val sliderWidth = if (isTotalScore) (screenWidth * 0.9).dp else (screenWidth * 0.45).dp
    val sliderHeight = if (isTotalScore) (screenHeight * 0.06).dp else (screenHeight * 0.025).dp
    val scoreTextWidth = (screenWidth * 0.12).dp

    // Optionally, total slider might have more step indicators. Using 10 steps for total, 5 otherwise.
    val stepCount = if (isTotalScore) 10 else 5

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // For subscores show the category text.
        if (!isTotalScore) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
        }

        // Draw the custom slider.
        Canvas(
            modifier = Modifier
                .width(sliderWidth)
                .height(sliderHeight)
                .padding(end = 15.dp)
        ) {
            val barHeight = if (isTotalScore) 12.dp.toPx() else 7.dp.toPx()
            val thumbRadius = if (isTotalScore) 10.dp.toPx() else 7.dp.toPx()
            val stepIndicatorRadius = if (isTotalScore) 3.dp.toPx() else 2.dp.toPx()
            val offsetY = (size.height - barHeight) / 2
            val activeWidth = fraction * size.width
            val stepSpacing = size.width / stepCount
            val cornerRadius = CornerRadius(3.5.dp.toPx(), 3.5.dp.toPx())

            // Inactive track.
            drawRoundRect(
                color = if (isTotalScore) Color(0xFFD8B4FE) else Color(0xFF8BC34A),
                topLeft = Offset(0f, offsetY),
                size = Size(size.width, barHeight),
                cornerRadius = cornerRadius
            )

            // Active track.
            drawRoundRect(
                color = if (isTotalScore) Color(0xFF7E22CE) else Color(0xFF0F4D00),
                topLeft = Offset(0f, offsetY),
                size = Size(activeWidth, barHeight),
                cornerRadius = cornerRadius
            )

            // Draw step indicators.
            repeat(stepCount) { index ->
                val stepX = (index + 1) * stepSpacing
                drawCircle(
                    color = if (stepX <= activeWidth) Color.White else {
                        if (isTotalScore) Color(0xFF7E22CE) else Color(0xFF0F4D00)
                    },
                    radius = stepIndicatorRadius,
                    center = Offset(stepX, size.height / 2)
                )
            }

            // Draw the thumb.
            drawCircle(
                color = Color.White,
                radius = thumbRadius,
                center = Offset(activeWidth, size.height / 2)
            )
            drawCircle(
                color = if (isTotalScore) Color(0xFF7E22CE) else Color(0xFF0F4D00),
                radius = thumbRadius,
                style = Stroke(width = if (isTotalScore) 3.dp.toPx() else 2.dp.toPx()),
                center = Offset(activeWidth, size.height / 2)
            )
        }

        // Display the current score for subscores with a fixed width.
        if (!isTotalScore) {
            Text(
                text = "${"%.1f".format(score)}/${"%.0f".format(maxScore)}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.width(scoreTextWidth), // Fixed width ensures alignment

            )
        }
    }
}
