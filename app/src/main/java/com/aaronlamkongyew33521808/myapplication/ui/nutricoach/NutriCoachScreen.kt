package com.aaronlamkongyew33521808.myapplication.ui.nutricoach

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
    viewModel: NutriCoachViewModel,
    navController: NavHostController,
    onReturnHome: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val fruits by viewModel.fruits.collectAsState()
    val genTip by viewModel.genTip.collectAsState()
    val tips by viewModel.tips.collectAsState()
    var fruitQuery by rememberSaveable { mutableStateOf("") }
    var showTipsDialog by rememberSaveable { mutableStateOf(false) }

    var filtered by rememberSaveable { mutableStateOf<List<Fruit>>(emptyList()) }

    val isOptimal by viewModel.isFruitOptimal.collectAsState()
    val randomImageUrl by viewModel.randomImageUrl.collectAsState()

    val loading   by viewModel.loading.collectAsState()

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
//                modifier = Modifier.height((screenHeight * 0.1).dp),
            )
        },
        bottomBar = { BottomBar(navController, userId, screenWidth, screenHeight) }

    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding((screenWidth * 0.04).dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (isOptimal == true) {
                    AsyncImage(
                        model = randomImageUrl,
                        contentDescription = "Motivational",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((screenHeight * 0.4).dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = fruitQuery,
                            onValueChange = { fruitQuery = it },
                            label = { Text("Search fruit…") },
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    filtered = fruits.filter {
                                        it.name.equals(fruitQuery.trim(), ignoreCase = true)
                                    }
                                }) {
                                    Icon(Icons.Default.Search, null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(16.dp))

                        if (filtered.isNotEmpty()) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(filtered) { fruit ->
                                    ElevatedCard(
                                        modifier = Modifier
                                            .width(200.dp)
                                            .height(180.dp),
                                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                                    ) {
                                        Column(
                                            Modifier
                                                .padding(12.dp)
                                                .fillMaxSize(),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(fruit.name, fontWeight = FontWeight.Bold)
                                            Text("Family: ${fruit.family}", style = MaterialTheme.typography.bodySmall)

                                            // now show all nutritional fields in two columns:
                                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                Column {
                                                    Text("Cal: ${fruit.nutritions.calories}", style = MaterialTheme.typography.bodySmall)
                                                    Text("Fat: ${fruit.nutritions.fat}", style = MaterialTheme.typography.bodySmall)
                                                    Text("Sugar: ${fruit.nutritions.sugar}", style = MaterialTheme.typography.bodySmall)
                                                }
                                                Column {
                                                    Text("Carbs: ${fruit.nutritions.carbohydrates}", style = MaterialTheme.typography.bodySmall)
                                                    Text("Prot: ${fruit.nutritions.protein}", style = MaterialTheme.typography.bodySmall)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Divider()


            Divider()

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("AI Motivational Tip", style = MaterialTheme.typography.titleMedium)

                genTip?.let {
                    Card(
                        Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Text(it, Modifier.padding(12.dp))
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        viewModel.generateTip(userId);
                    },
                        enabled = !loading
                    ) {
                        Text(if (loading)"Analysing your health…" else "Get personalised AI Tip")
                    }
                    TextButton(onClick = { showTipsDialog = true }) {
                        Text("Show All Tips")
                    }
                }

                if (showTipsDialog) {
                    AlertDialog(
                        onDismissRequest = { showTipsDialog = false },
                        title = { Text("AI Tip History") },
                        text = {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(tips) { tip ->
                                    Card(Modifier.fillMaxWidth()) {
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
