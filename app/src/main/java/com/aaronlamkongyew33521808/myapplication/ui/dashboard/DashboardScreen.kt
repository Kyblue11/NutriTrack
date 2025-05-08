package com.aaronlamkongyew33521808.myapplication.ui.dashboard

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aaronlamkongyew33521808.myapplication.R
import com.aaronlamkongyew33521808.myapplication.data.entity.QuestionnaireEntity
import com.aaronlamkongyew33521808.myapplication.ui.navigation.Routes
import androidx.navigation.NavController
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userId: String,
    navController: NavController,
    vm: QuestionnaireViewModel = viewModel()
) {
    // load once
    LaunchedEffect(userId) { vm.load(userId) }

    // observe state
    val fruits      by vm.fruits.collectAsState()
    val vegetables  by vm.vegetables.collectAsState()
    val grains      by vm.grains.collectAsState()
    val redMeat     by vm.redMeat.collectAsState()
    val seafood     by vm.seafood.collectAsState()
    val poultry     by vm.poultry.collectAsState()
    val fish        by vm.fish.collectAsState()
    val eggs        by vm.eggs.collectAsState()
    val nutsSeeds   by vm.nutsSeeds.collectAsState()

    var showPersonaDialog by remember { mutableStateOf(false) }
    val persona         by vm.persona.collectAsState()

    val biggestMealTime by vm.biggestMealTime.collectAsState()
    val sleepTime       by vm.sleepTime.collectAsState()
    val wakeTime        by vm.wakeTime.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Intake Questionnaire", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick =  { navController.popBackStack() } )
                    {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Tick all the food categories you can eat",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            FoodCategoriesRow1(fruits, vm::setFruits, vegetables, vm::setVegetables, grains, vm::setGrains)
            FoodCategoriesRow2(redMeat = vm::setRedMeat, seafood = vm::setSeafood, poultry = vm::setPoultry)
            FoodCategoriesRow3(fish = vm::setFish, eggs = vm::setEggs, nutsSeeds = vm::setNutsSeeds)

            Spacer(modifier = Modifier.height(20.dp))
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Your Persona",
                style = TextStyle(fontSize = 16.sp),
            )
            Text(
                text = "People can be broadly classified into 6 different types based on their eating preferences. " +
                        "Click on each button below to find out the different types, and select the type that best fits you!",
                style = TextStyle(fontSize = 14.sp),
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            FlowRowPersonaButtons(personaList = personaList,    onPersonaClick =  { vm.setPersona(it) } )

            Spacer(modifier = Modifier.height(20.dp))
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Which persona best fits you?")
            Spacer(modifier = Modifier.height(4.dp))

            PersonaDropdown(personaList = personaList, selectedPersona = persona, onPersonaSelected = vm::setPersona)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Timings",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TimingRow("Biggest meal time", biggestMealTime, vm::setBiggestMealTime)
            TimingRow("Sleep time", sleepTime, vm::setSleepTime)
            TimingRow("Wake time", wakeTime, vm::setWakeTime)

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    vm.save(userId) {
                        navController.navigate(Routes.Home.replace("{userId}", userId)) {
                            popUpTo(Routes.Dashboard.replace("/{userId}", "")) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save", fontSize = 16.sp)
            }
        }
    }

    if (showPersonaDialog) {
        val personaImage = getPersonaImage(persona)
        val personaDesc = getPersonaDescription(persona)

        // persona modal boxes
        AlertDialog(
            onDismissRequest = { showPersonaDialog = false },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(personaImage),
                        contentDescription = persona,
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = persona,
                        style = TextStyle(fontSize = 18.sp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = personaDesc,
                        style = TextStyle(fontSize = 14.sp),
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showPersonaDialog = false }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

//////////////////////////////////////////////////////////////////////////////////////////
// hard-coded parts
//////////////////////////////////////////////////////////////////////////////////////////

val personaList = listOf(
    "Health Devotee",
    "Mindful Eater",
    "Wellness Striver",
    "Balance Seeker",
    "Health Procrastinator",
    "Food Carefree"
)

@Composable
fun getPersonaImage(personaName: String): Int {
    return when(personaName) {
        "Health Devotee" -> R.drawable.persona_1
        "Mindful Eater" -> R.drawable.persona_2
        "Wellness Striver" -> R.drawable.persona_3
        "Balance Seeker" -> R.drawable.persona_4
        "Health Procrastinator" -> R.drawable.persona_5
        "Food Carefree" -> R.drawable.persona_6
        else -> R.drawable.persona_1
    }
}

@Composable
fun getPersonaDescription(personaName: String): String {
    return when(personaName) {
        "Health Devotee" -> "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy."
        "Mindful Eater" -> "I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media."
        "Wellness Striver" -> "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go."
        "Balance Seeker" -> "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips."
        "Health Procrastinator" -> "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life."
        "Food Carefree" -> "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat."
        else -> "GG error."
    }
}

@Composable
fun FoodCategoriesRow1(
    fruits: Boolean, onFruitsChange: (Boolean) -> Unit,
    vegetables: Boolean, onVegetablesChange: (Boolean) -> Unit,
    grains: Boolean, onGrainsChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = fruits, onCheckedChange = onFruitsChange)
        Text("Fruits", modifier = Modifier.padding(end = 16.dp))
        Checkbox(checked = vegetables, onCheckedChange = onVegetablesChange)
        Text("Vegetables", modifier = Modifier.padding(end = 16.dp))
        Checkbox(checked = grains, onCheckedChange = onGrainsChange)
        Text("Grains")
    }
}

@Composable
fun FoodCategoriesRow2(
    redMeat: Boolean, onRedMeatChange: (Boolean) -> Unit,
    seafood: Boolean, onSeafoodChange: (Boolean) -> Unit,
    poultry: Boolean, onPoultryChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = redMeat, onCheckedChange = onRedMeatChange)
        Text("Red Meat", modifier = Modifier.padding(end = 16.dp))
        Checkbox(checked = seafood, onCheckedChange = onSeafoodChange)
        Text("Seafood", modifier = Modifier.padding(end = 16.dp))
        Checkbox(checked = poultry, onCheckedChange = onPoultryChange)
        Text("Poultry")
    }
}

@Composable
fun FoodCategoriesRow3(
    fish: Boolean, onFishChange: (Boolean) -> Unit,
    eggs: Boolean, onEggsChange: (Boolean) -> Unit,
    nutsSeeds: Boolean, onNutsSeedsChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = fish, onCheckedChange = onFishChange)
        Text("Fish", modifier = Modifier.padding(end = 16.dp))
        Checkbox(checked = eggs, onCheckedChange = onEggsChange)
        Text("Eggs", modifier = Modifier.padding(end = 16.dp))
        Checkbox(checked = nutsSeeds, onCheckedChange = onNutsSeedsChange)
        Text("Nuts/Seeds")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowPersonaButtons(
    personaList: List<String>,
    onPersonaClick: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        personaList.forEach { persona ->
            Button(
                onClick = { onPersonaClick(persona) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = persona, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonaDropdown(
    personaList: List<String>,
    selectedPersona: String,
    onPersonaSelected: (String) -> Unit
) {
    // to remember the state of the dropdown menu
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (selectedPersona.isEmpty()) "Select option" else selectedPersona,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            personaList.forEach { persona ->
                DropdownMenuItem(
                    text = { Text(persona) },
                    onClick = {
                        onPersonaSelected(persona)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun TimingRow(
    label: String,
    timeValue: String,
    onTimeSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)
    ) {
        Text(text = label, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedButton(
            onClick = { showDialog = true },
            modifier = Modifier.heightIn(min = 48.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            // so that the oval button will not be em
            val displayText = if (timeValue.isEmpty()) "Select time" else timeValue

            Text(
                text = displayText,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (showDialog) {
            showTimePickerDialog { newTime ->
                onTimeSelected(newTime)
                showDialog = false
            }
        }
    }
}

@Composable
private fun showTimePickerDialog(onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val context = LocalContext.current
    TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        },
        hour, minute, true
    ).show()
}