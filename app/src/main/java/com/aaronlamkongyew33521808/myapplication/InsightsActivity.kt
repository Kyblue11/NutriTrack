package com.aaronlamkongyew33521808.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aaronlamkongyew33521808.myapplication.ui.theme.NutriTrackTheme

class InsightsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = getSharedPreferences("NutriTrackPrefs", MODE_PRIVATE)
        val userId = intent.getStringExtra("userId")
            ?: sp.getString("userId", "Guest") ?: "Guest"
        val csvRecords = loadCSV(this)
        val userRecord = csvRecords.find { it.userId == userId }

        val sex = if (userRecord?.HEIFAtotalscoreMale != 0.0) "male"
        else "female"

        val mainTotalScore = userRecord?.let { row ->
            if (sex.equals("male", ignoreCase = true)) row.HEIFAtotalscoreMale
            else row.HEIFAtotalscoreFemale
        } ?: 0.0

        val subScores = getSubScores(userRecord, sex)

        setContent {
            NutriTrackTheme {
                InsightsScreen(
                    mainTotalScore = mainTotalScore,
                    subScores = subScores,

                    onShareClick = {
                        val textToShare = "My Food Quality Score is ${"%.2f".format(mainTotalScore)}/100. " + "Check out my other scores!"
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, textToShare)
                        }
                        startActivity(Intent.createChooser(shareIntent, "Share text via"))
                    },
                    onImproveClick = {  },

                    onNavigateHome = { finish() },
                    onNavigateInsights = {  },
                    onNavigateNutriCoach = {   },
                    onNavigateSettings = {   }
                )
            }
        }
    }
}

private fun getSubScores(record: UserData?, sex: String): Map<String, Double> {
    if (record == null) return emptyMap()
    val male = sex.equals("male", ignoreCase = true)

    return mapOf(
        "Vegetables" to if (male) record.vegetablesHEIFAscoreMale else record.vegetablesHEIFAscoreFemale,
        "Fruits" to if (male) record.fruitHEIFAscoreMale else record.fruitHEIFAscoreFemale,
        "Grains & Cereals" to if (male) record.grainsAndCerealsHEIFAscoreMale else record.grainsAndCerealsHEIFAscoreFemale,
        "Whole Grains" to if (male) record.wholegrainsHEIFAscoreMale else record.wholegrainsHEIFAscoreFemale,
        "Meat & Alternatives" to if (male) record.meatAndAlternativesHEIFAscoreMale else record.meatAndAlternativesHEIFAscoreFemale,
        "Dairy" to if (male) record.dairyAndAlternativesHEIFAscoreMale else record.dairyAndAlternativesHEIFAscoreFemale,
        "Water" to if (male) record.waterHEIFAscoreMale else record.waterHEIFAscoreFemale,
        "Unsaturated Fats" to if (male) record.unsaturatedFatHEIFAscoreMale else record.unsaturatedFatHEIFAscoreFemale,
        "Sodium" to if (male) record.sodiumHEIFAscoreMale else record.sodiumHEIFAscoreFemale,
        "Sugar" to if (male) record.sugarHEIFAscoreMale else record.sugarHEIFAscoreFemale,
        "Alcohol" to if (male) record.alcoholHEIFAscoreMale else record.alcoholHEIFAscoreFemale,
        "Discretionary Foods" to if (male) record.discretionaryHEIFAscoreMale else record.discretionaryHEIFAscoreFemale
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(

    mainTotalScore: Double,
    subScores: Map<String, Double>,
    onShareClick: () -> Unit,
    onImproveClick: () -> Unit,

    onNavigateHome: () -> Unit,
    onNavigateInsights: () -> Unit,
    onNavigateNutriCoach: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                    text = "Insights: Food Score",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )},
                navigationIcon = {
                    IconButton(onClick = onNavigateHome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateHome,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = onNavigateInsights,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.insights),
                            contentDescription = "Insights"
                        )
                    },
                    label = { Text("Insights") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateNutriCoach,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.coach),
                            contentDescription = "NutriCoach"
                        )
                    },
                    label = { Text("NutriCoach") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateSettings,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = "Settings"
                        )
                    },
                    label = { Text("Settings") }
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            InsightsContent(
                mainTotalScore = mainTotalScore,
                subScores = subScores,
                onShareClick = onShareClick,
                onImproveClick = onImproveClick
            )
        }
    }
}

@Composable
fun InsightsContent(
    mainTotalScore: Double,
    subScores: Map<String, Double>,
    onShareClick: () -> Unit,
    onImproveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        subScores.forEach { (label, value) ->
            ScoreRowWithSlider(
                category = label,
                score = value,

            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total Food Quality Score",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        val totalProgress = (mainTotalScore / 100).coerceIn(0.0, 1.0).toFloat()
        Slider(
            value = totalProgress,
            onValueChange = {  },
            valueRange = 0f..1f,
            enabled = false,
            colors = SliderDefaults.colors(
                disabledThumbColor = MaterialTheme.colorScheme.primary,
                disabledActiveTrackColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = "${"%.2f".format(mainTotalScore)}/100",
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onShareClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.share),
                contentDescription = "Share"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Share with someone")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onImproveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.coach),
                contentDescription = "Improve"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Improve my diet!")
        }
    }
}

@Composable
fun ScoreRowWithSlider(
    category: String,
    score: Double,
) {
    val fivePointCategories = listOf("Grains & Cereals", "Whole Grains", "Water", "Alcohol")
    val maxScore = if (category in fivePointCategories) 5.0 else 10.0
    val fraction = (score / maxScore).coerceIn(0.0, 1.0).toFloat()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        Slider(
            value = fraction,
            onValueChange = {},
            valueRange = 0f..1f,
            enabled = false,
            modifier = Modifier.weight(2f).height(4.dp),
            colors = SliderDefaults.colors(
                disabledThumbColor = MaterialTheme.colorScheme.primary,
                disabledActiveTrackColor = MaterialTheme.colorScheme.primary,
            )
        )

        Text(
            text = "${"%.1f".format(score)}/${"%.0f".format(maxScore)}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

