package com.aaronlamkongyew33521808.myapplication.ui.navigation


import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
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
fun BottomBar(navController: NavHostController, userId: String) {
    val items = listOf(Screen.Home, Screen.Insights, Screen.NutriCoach, Screen.Settings)
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            val routePrefix = screen.route.substringBefore("/{")
            NavigationBarItem(
                icon = { Icon(painterResource(screen.icon), contentDescription = screen.label) },
                label = { androidx.compose.material3.Text(screen.label) },
                selected = currentRoute?.startsWith(routePrefix) == true,
                onClick = {
                    navController.navigate(screen.route.replace("{userId}", userId)) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState     = true
                    }
                }
            )
        }
    }
}