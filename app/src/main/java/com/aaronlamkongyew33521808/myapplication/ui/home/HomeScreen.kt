package com.aaronlamkongyew33521808.myapplication.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aaronlamkongyew33521808.myapplication.R
import com.aaronlamkongyew33521808.myapplication.viewmodel.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aaronlamkongyew33521808.myapplication.ui.navigation.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userId: String,
    onEditClick: () -> Unit,
    onInsights: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController
) {
    // load once
    LaunchedEffect(userId) { viewModel.load(userId) }

    // observe
    val userId by viewModel.userId.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val score by viewModel.foodQualityScore.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Dashboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },

                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "HamburgerMenu"
                        )
                    }
                },
            )
        },

        bottomBar = { BottomBar(navController, userId) }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Greeting Section
            Text(text = "Hello,",
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text =
                "User $userId, $userName" ,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "You’ve already filled in your questionnaire, but you can change details below:",
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))
            OutlinedButton(onClick = onEditClick) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                Text("Edit")
            }

            Spacer(Modifier.height(24.dp))

            // Score Section
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My Score", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                TextButton(onClick = onInsights) {
                    Text("See all scores ➔")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Purple Mascot",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(end = 16.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Your Food Quality score",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    val color = when {
                        score >= 66.7 -> Color(0xFF4CAF50)
                        score >= 33.3 -> Color(0xFFFFC107)
                        else -> Color(0xFFF44336)
                    }
                    Text(
                        text = "${"%.2f".format(score)}/100",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

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
    }
}