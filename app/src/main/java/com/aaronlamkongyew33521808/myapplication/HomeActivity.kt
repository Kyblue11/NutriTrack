package com.aaronlamkongyew33521808.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import com.aaronlamkongyew33521808.myapplication.ui.theme.NutriTrackTheme

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = getSharedPreferences("NutriTrackPrefs", MODE_PRIVATE)
        val userId = intent.getStringExtra("userId") ?: sp.getString("userId", "Guest") ?: "Guest"
        val records = loadCSV(this)
        val userRecord = records.find { it.userId == userId }
        val scoreToShow = userRecord?.let { row ->
            if (row.sex == "Male") {
                row.HEIFAtotalscoreMale
            } else {
                row.HEIFAtotalscoreFemale
            }
        } ?: 0.0
        setContent {
            NutriTrackTheme {
                HomeScreen(
                    userName = userId,
                    foodQualityScore = scoreToShow,
                    onEditClick = {
                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.putExtra("userId", userId) // remember previous options selected
                        startActivity(intent)
                    },
                    onNavigateInsights = {
                        val intent = Intent(this, InsightsActivity::class.java)
                        startActivity(intent)
                    },
                    onNavigateNutriCoach = {  },
                    onNavigateSettings = {  }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    foodQualityScore: Double,
    onEditClick: () -> Unit,
    onNavigateInsights: () -> Unit,
    onNavigateNutriCoach: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "My Dashboard",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold
                    )
                ) },
                navigationIcon = {
                    IconButton(onClick = { /* handle nav drawer or back? */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                onHomeClick = { /* Already on Home, do nothing */ },
                onInsightsClick = onNavigateInsights,
                onNutriCoachClick = onNavigateNutriCoach,
                onSettingsClick = onNavigateSettings
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeContent(
                userName = userName,
                score = foodQualityScore,
                onEditClick = onEditClick,
                onSeeAllScoresClick = onNavigateInsights
            )
        }
    }
}

@Composable
fun HomeContent(
    userName: String,
    score: Double,
    onEditClick: () -> Unit,
    onSeeAllScoresClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GreetingSection(userName = userName, onEditClick = onEditClick)

        Spacer(modifier = Modifier.height(16.dp))

        ScoreSection(score = score, onSeeAllScoresClick = onSeeAllScoresClick)

        Spacer(modifier = Modifier.height(16.dp))

        ExplanationSection()
    }
}

@Composable
fun GreetingSection(
    userName: String,
    onEditClick: () -> Unit
) {
    Column {
        Text(
            text = "Hello,",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "You’ve already filled in your Food Intake Questionnaire, but you can change details here:",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onEditClick,
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Edit")
        }
    }
}

@Composable
fun ScoreSection(score: Double, onSeeAllScoresClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My Score",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        TextButton(onClick = onSeeAllScoresClick) {
            Text(text = "See all scores ➔")
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Plate image??",
            modifier = Modifier
                .size(150.dp)
                .padding(end = 16.dp)
        )

        Column {
            Text(
                text = "Your Food Quality score",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            val scoreColor = when {
                score >= 66.7 -> Color(0xFF4CAF50)
                score >= 33.3 -> Color(0xFFFFC107)
                else -> Color(0xFFF44336)
            }
            Text(
                text = "${"%.2f".format(score)}/100",
                style = MaterialTheme.typography.titleLarge.copy(color = scoreColor)
            )
        }
    }
}

@Composable
fun ExplanationSection() {
    Text(
        text = "What is the Food Quality Score?",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Text(
        text = """
            Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet.
            
            This personalized measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.
        """.trimIndent(),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Justify
    )
}

@Composable
fun BottomNavBar(
    onHomeClick: () -> Unit,
    onInsightsClick: () -> Unit,
    onNutriCoachClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // BottomAppBar might be better for within-screen navigation
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = onHomeClick,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onInsightsClick,
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
            onClick = onNutriCoachClick,
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
            onClick = onSettingsClick,
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
