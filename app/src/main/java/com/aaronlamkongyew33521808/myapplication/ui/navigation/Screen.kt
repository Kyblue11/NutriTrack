package com.aaronlamkongyew33521808.myapplication.ui.navigation


import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aaronlamkongyew33521808.myapplication.R

/**
 * Sealed screen definitions including route, icon and label.
 */
sealed class Screen(val route: String, @DrawableRes val icon: Int, val label: String) {
    object Home      : Screen("home/{userId}",      R.drawable.home,      "Home")
    object Insights  : Screen("insights/{userId}",  R.drawable.insights,  "Insights")
    object NutriCoach: Screen("coach/{userId}",     R.drawable.coach,     "NutriCoach")
    object Settings  : Screen("settings/{userId}",  R.drawable.settings,  "Settings")
}

/**
 * Bottom navigation bar driven by NavController.
 */
@Composable
fun BottomBar(navController: NavHostController, userId: String, screenWidth: Int, screenHeight: Int) {
    val items = listOf(Screen.Home, Screen.Insights, Screen.NutriCoach, Screen.Settings)
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    NavigationBar(
        //modifier = Modifier.height((screenHeight * 0.08).dp)
    ) {
        items.forEach { screen ->
            val routePrefix = screen.route.substringBefore("/{")
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(screen.icon),
                        contentDescription = screen.label,
                        modifier = Modifier.size((screenWidth * 0.07).dp)
                    )
                },
                label = {
                    Text(
                        screen.label,
                        fontSize = (screenWidth * 0.035).sp
                    )
                },
                selected = currentRoute?.startsWith(routePrefix) == true,
                onClick = {
                    navController.navigate(screen.route.replace("{userId}", userId)) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}