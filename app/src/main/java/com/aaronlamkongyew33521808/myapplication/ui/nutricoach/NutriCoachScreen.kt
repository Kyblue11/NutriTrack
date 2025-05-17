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
        topBar = { TopAppBar(
            title = {
                Text(
                    text = "NutriCoach",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },) },

        bottomBar = { BottomBar(navController, userId) }

    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            item {

                if (isOptimal == false || isOptimal == null) {
                    Text("Fruit Name")
                    TextField(
                        value = fruitQuery,
                        onValueChange = { fruitQuery = it },
                        label = { Text("e.g. banana") },
                        singleLine = true
                    )
                    Button(onClick = { /* filter fruits */

                        filtered = fruits.filter {
                            it.name.contains(fruitQuery.trim(), ignoreCase = true) &&
                                    fruitQuery.trim().length == it.name.length
                        }

                    }) { Text("Details") }

                    // Display the filtered fruits
                    if (filtered.isNotEmpty()) {
                        filtered.forEach { fruit ->
                            Card(modifier = Modifier.padding(8.dp)) {
                                Column {
                                    Text(fruit.name, fontWeight = FontWeight.Bold)
                                    Text("family: ${fruit.family}")
                                    Text("Calories: ${fruit.nutritions.calories}")
                                    Text("Fat: ${fruit.nutritions.fat}")
                                    Text("Sugar: ${fruit.nutritions.sugar}")
                                    Text("Carbohydrates: ${fruit.nutritions.carbohydrates}")
                                    Text("Protein: ${fruit.nutritions.protein}")


                                }
                            }
                        }
                    }

                } else {
                    Text("You are doing great! Here's a motivational picture.")
                    AsyncImage(
                        model = randomImageUrl,
                        // TODO: check why is image not random?
                        contentDescription = "random pic",
                        modifier = Modifier.fillMaxWidth().height(350.dp)
                    )
                }

                Spacer(Modifier.height(24.dp))
                Divider(modifier = Modifier.padding(vertical = 16.dp))

                Text("🤖 AI Motivational Tip")
                Button(onClick = {
                    viewModel.generateTip(
                        userId
                    )
                }) {
                    Text("Get AI Tip")
                }

                genTip?.let {
                    Card(modifier = Modifier.padding(8.dp)) {
                        Text(it, Modifier.padding(8.dp))
                    }
                }

                Button(onClick = { showTipsDialog = true }) {
                    Text("Show All Tips")
                }

                if (showTipsDialog) {
                    AlertDialog(
                        onDismissRequest = { showTipsDialog = false },
                        title = { Text("AI Tip History") },
                        text = {
                            LazyColumn {
                                items(
                                    tips) { tip ->
                                    Card(modifier = Modifier.padding(8.dp)) {
                                        Text(tip.tip, Modifier.padding(8.dp))
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showTipsDialog = false }) {
                                Text("Close")
                            }
                        }
                    )
                }
            }
        }
    }
}
