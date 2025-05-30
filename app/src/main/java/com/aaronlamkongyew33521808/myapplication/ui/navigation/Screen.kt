package com.aaronlamkongyew33521808.myapplication.ui.navigation


import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
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

    val context = LocalContext.current
    val prefs   = context.getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)

    val items = listOf(Screen.Home, Screen.Insights, Screen.NutriCoach, Screen.Settings)
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    val navBarHeight = if (screenHeight < screenWidth) (screenHeight * 0.2).dp else (screenHeight * 0.15).dp
    val iconSize = if (screenHeight < screenWidth) (screenWidth * 0.02).dp else (screenWidth * 0.05).dp
    val fontSize = if (screenHeight < screenWidth) (screenWidth * 0.015).sp else (screenWidth * 0.025).sp


    NavigationBar(
//        modifier = Modifier.height((screenHeight * 0.2).dp)
        modifier = Modifier.height(navBarHeight)
    ) {
        items.forEach { screen ->
            val route         = screen.route.replace("{userId}", userId)
            val routePrefix   = screen.route.substringBefore("/{")
            val isSelected    = currentRoute?.startsWith(routePrefix) == true
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(screen.icon),
                        contentDescription = screen.label,
                        modifier = Modifier.size(iconSize)
//                        modifier = Modifier.size((screenWidth * 0.07).dp)
                    )
                },
                label = {
                    Text(
                        screen.label,
                        fontSize = fontSize
//                        fontSize = (screenWidth * 0.035).sp
                    )
                },
                selected = currentRoute?.startsWith(routePrefix) == true,
                onClick = {
                    // prevent the spam click UI bug
                    if (!isSelected) {
                    navController.navigate(screen.route.replace("{userId}", userId)) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true

                    }} else {
                        // do nothing
                    }

                    prefs.edit()
                        .putString("lastRoute", route)
                        .apply()
                }
            )
        }
    }
}