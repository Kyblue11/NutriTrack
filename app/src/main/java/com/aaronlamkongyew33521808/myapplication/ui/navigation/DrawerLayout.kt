package com.aaronlamkongyew33521808.myapplication.ui.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aaronlamkongyew33521808.myapplication.auth.AuthManager
import kotlinx.coroutines.launch

@Composable
fun DrawerLayout(
    navController: NavHostController,
    userId: String?,
    content: @Composable (onMenuClick: () -> Unit) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    // 1) keep drawerState & scope here once for the whole app
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope       = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState   = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                Modifier.widthIn(max = screenWidth * 0.7f)
            ) {
                Spacer(Modifier.height(16.dp))
                NavigationDrawerItem(
               label =              {   Text(
                   text = "User Statistics",
                   fontWeight = FontWeight.Bold,
                     fontSize = 20.sp
               )     },
                    selected = false,
                    onClick = {}
                )

                NavigationDrawerItem(
                    label    = { Text("Home") },
                    selected = false,
                    onClick  = {
                        scope.launch { drawerState.close() }
                        // assume AuthManager.userId.value is your current ID
                        val id = if (userId == null) {
                            "1" // placeholder
                        } else {
                            userId
                        }
                        navController.navigate(Routes.Home.replace("{userId}", id))
                    }
                )

                NavigationDrawerItem(
                    label = { Text("All HEIFA Charts") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        val id = if (userId == null) {
                            "1"
                        } else {
                            userId
                        }
                        navController.navigate(Routes.StatsCombined.replace("{userId}", id))
                    }
                )
                // …etc
            }
        }
    ) {
        // 2) now wrap all app’s content, passing in how to open drawer
        content {
            scope.launch { drawerState.open() }
        }
    }
}