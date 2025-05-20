package com.aaronlamkongyew33521808.myapplication.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
    onMenuClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

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
//                modifier = Modifier.height((screenHeight * 0.1).dp)
            )
        },
        bottomBar = { BottomBar(navController, userId, screenWidth, screenHeight) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding((screenWidth * 0.04).dp),
            verticalArrangement = Arrangement.Top
        ) {
//            item {
                // Greeting Section
                Text(
                    text = "Hello,",
                    fontSize = (screenWidth * 0.045).sp,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "User $userId, $userName",
                    fontSize = (screenWidth * 0.06).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height((screenHeight * 0.03).dp))
                Text(
                    text = "You’ve already filled in your questionnaire, but you can change details below:",
                    fontSize = (screenWidth * 0.04).sp,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = (screenWidth * 0.05).sp
                    )
                )
                Spacer(Modifier.height((screenHeight * 0.02).dp))
                OutlinedButton(onClick = onEditClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = null,
                        modifier = Modifier.size((screenWidth * 0.05).dp)
                    )
                    Spacer(Modifier.width((screenWidth * 0.02).dp))
                    Text("Edit", fontSize = (screenWidth * 0.04).sp)
                }

                // Score Section
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "My Score",
                        fontSize = (screenWidth * 0.045).sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    TextButton(onClick = onInsights) {
                        Text(
                            "See all scores ➔",
                            fontSize = (screenWidth * 0.04).sp
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = "Purple Mascot",
                        modifier = Modifier
                            .size((screenWidth * 0.4).dp)
                            .padding(end = (screenWidth * 0.03).dp)
                    )
                    Column {
                        Text(
                            text = "Your Food Quality score",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = (screenWidth * 0.04).sp
                        )
                        val color = when {
                            score >= 66.7 -> Color(0xFF4CAF50)
                            score >= 33.3 -> Color(0xFFFFC107)
                            else -> Color(0xFFF44336)
                        }
                        Text(
                            text = "${"%.2f".format(score)}/100",
                            fontSize = (screenWidth * 0.08).sp,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                    }
                }

                Spacer(Modifier.height((screenHeight * 0.03).dp))
                Divider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    thickness = (screenHeight * 0.001).dp,
                    modifier = Modifier.padding(vertical = (screenHeight * 0.01).dp)
                )
                Text(
                    text = "What is the Food Quality Score?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    fontSize = (screenWidth * 0.045).sp,
                    modifier = Modifier.padding(vertical = (screenHeight * 0.01).dp)
                )
                Text(
                    text = """
                    Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet.
    
                    This personalized measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.
                """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = (screenWidth * 0.05).sp
                    ),
                    fontSize = (screenWidth * 0.04).sp,
                    textAlign = TextAlign.Justify
                )
//            }
        }
    }
}