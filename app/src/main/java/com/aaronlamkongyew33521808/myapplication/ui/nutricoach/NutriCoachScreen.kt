package com.aaronlamkongyew33521808.myapplication.ui.nutricoach

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.aaronlamkongyew33521808.myapplication.viewmodel.NutriCoachViewModel
import coil3.compose.rememberAsyncImagePainter
import com.aaronlamkongyew33521808.myapplication.data.api.Fruit
import com.aaronlamkongyew33521808.myapplication.ui.navigation.BottomBar
import com.aaronlamkongyew33521808.myapplication.viewmodel.NutriCoachViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutriCoachScreen(
    userId: String,
    viewModel: NutriCoachViewModel, // TODO: need argument viewModel(factory = ...) ? Check with NutriCoachViewModel
    navController: NavHostController
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val fruits by viewModel.fruits.collectAsState()
    val genTip by viewModel.genTip.collectAsState()
    val tips by viewModel.tips.collectAsState()
    var fruitQuery by remember { mutableStateOf("") }
    var showTipsDialog by remember { mutableStateOf(false) }

    var filtered by remember { mutableStateOf<List<Fruit>>(emptyList()) }

    val isOptimal by viewModel.isFruitOptimal.collectAsState()
    val randomImageUrl by viewModel.randomImageUrl.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFruits()
        viewModel.loadTipHistory(userId)
        viewModel.checkFruitOptimal(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "NutriCoach",
                        fontWeight = FontWeight.Bold,
                        fontSize = (screenWidth * 0.05).sp
                    )
                },
//                modifier = Modifier.height((screenHeight * 0.1).dp),
            )
        },
        bottomBar = { BottomBar(navController, userId, screenWidth, screenHeight) }

    ) { padding ->
        LazyColumn(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding((screenWidth * 0.04).dp)
        ) {
            item {

                if (isOptimal == false || isOptimal == null) {
                    Text(
                        "Fruit Name",
                        fontSize = (screenWidth * 0.04).sp
                    )
                    TextField(
                        value = fruitQuery,
                        onValueChange = { fruitQuery = it },
                        label = { Text("e.g. banana", fontSize = (screenWidth * 0.035).sp,
                            modifier = Modifier.padding(vertical = (screenHeight * 0.01).dp)) },
                        singleLine = true
                    )

                    Spacer(Modifier.height((screenHeight * 0.01).dp))

                    Button(onClick = {
                        filtered = fruits.filter {
                            it.name.contains(fruitQuery.trim(), ignoreCase = true) &&
                                    fruitQuery.trim().length == it.name.length
                        }
                    }) {
                        Text("Details", fontSize = (screenWidth * 0.03).sp)
                    }

                    if (filtered.isNotEmpty()) {
                        Spacer(Modifier.height((screenHeight * 0.02).dp))

                        filtered.forEach { fruit ->
                            Card(modifier = Modifier.padding((screenWidth * 0.03).dp)) {
                                Column {
                                    Text(fruit.name, fontWeight = FontWeight.Bold, fontSize = (screenWidth * 0.04).sp)
                                    Text("family: ${fruit.family}", fontSize = (screenWidth * 0.035).sp)
                                    Text("Calories: ${fruit.nutritions.calories}", fontSize = (screenWidth * 0.035).sp)
                                    Text("Fat: ${fruit.nutritions.fat}", fontSize = (screenWidth * 0.035).sp)
                                    Text("Sugar: ${fruit.nutritions.sugar}", fontSize = (screenWidth * 0.035).sp)
                                    Text("Carbohydrates: ${fruit.nutritions.carbohydrates}", fontSize = (screenWidth * 0.035).sp)
                                    Text("Protein: ${fruit.nutritions.protein}", fontSize = (screenWidth * 0.035).sp)
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        "You are doing great! Here's a motivational picture.",
                        fontSize = (screenWidth * 0.035).sp
                    )
                    AsyncImage(
                        model = randomImageUrl,
                        contentDescription = "random pic",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((screenHeight * 0.4).dp)
                    )
                }

//                Spacer(Modifier.height((screenHeight * 0.01).dp))
                Divider(modifier = Modifier.padding(vertical = (screenHeight * 0.02).dp))

                Text(
                    "AI Motivational Tip",
                    fontSize = (screenWidth * 0.045).sp
                )
                Spacer(Modifier.height((screenHeight * 0.03).dp))

                genTip?.let {
                    Card(modifier = Modifier.padding((screenWidth * 0.02).dp)) {
                        Text(
                            it,
                            Modifier.padding((screenWidth * 0.02).dp),
                            fontSize = (screenWidth * 0.04).sp,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = (screenWidth * 0.05).sp
                            )
                        )                    }
                }

                Button(onClick = {
                    viewModel.generateTip(userId)
                }) {
                    Text("Get AI Tip", fontSize = (screenWidth * 0.03).sp)
                }

                Spacer(Modifier.height((screenHeight * 0.01).dp))

                Button(onClick = { showTipsDialog = true }) {
                    Text("Show All Tips", fontSize = (screenWidth * 0.03).sp)
                }

                if (showTipsDialog) {
                    AlertDialog(
                        onDismissRequest = { showTipsDialog = false },
                        title = { Text("AI Tip History", fontSize = (screenWidth * 0.045).sp) },
                        text = {
                            LazyColumn {
                                items(tips) { tip ->
                                    Card(modifier = Modifier.padding((screenWidth * 0.02).dp)) {
                                        Text(
                                            tip.tip,
                                            Modifier.padding((screenWidth * 0.02).dp),
                                            fontSize = (screenWidth * 0.04).sp,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                lineHeight = (screenWidth * 0.05).sp
                                            )
                                        )
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showTipsDialog = false }) {
                                Text("Close", fontSize = (screenWidth * 0.04).sp)
                            }
                        }
                    )
                }
            }
        }
    }
}
