package com.aaronlamkongyew33521808.myapplication.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import com.aaronlamkongyew33521808.myapplication.R
import com.aaronlamkongyew33521808.myapplication.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userId: String,
    onEditClick: () -> Unit,
    onInsights: () -> Unit,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // load once
    LaunchedEffect(userId) { viewModel.load(userId) }

    // observe
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
                }
            )
        },
        bottomBar = {
            // You can keep your BottomNavBar here if you like
            // just pass onInsights to its Insights button
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Greeting Section
            Text(text = "Hello,", fontSize = 18.sp)
            Text(text = userName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "You’ve already filled in your questionnaire, but you can change details here:",
                fontSize = 14.sp
            )
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onEditClick) {
                Icon(
                    painter = painterResource(id = R.drawable.settings), // TODO: R.drawable.edit
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
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Your Food Quality score", fontSize = 14.sp)
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

            // Explanation
            Text(
                text = "What is the Food Quality Score?",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established guidelines...",
                fontSize = 14.sp,
                textAlign = TextAlign.Justify
            )
        }
    }
}