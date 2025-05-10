package com.aaronlamkongyew33521808.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aaronlamkongyew33521808.myapplication.ui.dashboard.DashboardScreen
import com.aaronlamkongyew33521808.myapplication.ui.home.HomeScreen
import com.aaronlamkongyew33521808.myapplication.ui.insights.InsightsScreen
import com.aaronlamkongyew33521808.myapplication.ui.login.LoginScreen
import com.aaronlamkongyew33521808.myapplication.ui.register.RegisterScreen
import com.aaronlamkongyew33521808.myapplication.ui.welcome.WelcomeScreen
import com.aaronlamkongyew33521808.myapplication.viewmodel.InsightsViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.LoginViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.RegisterViewModel

object Routes {
    const val Welcome = "welcome"
    const val Login = "login"
    const val Register = "register"
    const val Dashboard = "dashboard/{userId}" // pass userId
    const val Home = "home/{userId}"
    const val Insights = "insights/{userId}"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Welcome) {

        composable(Routes.Welcome) {
            WelcomeScreen {
                navController.navigate(Routes.Login) {
//                    popUpTo(Routes.Welcome) { inclusive = true }
                }
            }
        }

        composable(Routes.Login) {
            val vm: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = vm,
                onLoginSuccess = { id -> navController.navigate("dashboard/$id")
//                    {
//                        popUpTo(Routes.Login) { inclusive = true } // TODO: should i remove this?
//                    }
                 },
                onRegister = { navController.navigate(Routes.Register) }
            )
        }
        composable(Routes.Register) {
            val vm: RegisterViewModel = viewModel()
            RegisterScreen(
                viewModel = vm,
                onDone = { navController.navigate(Routes.Login) {
                    popUpTo(Routes.Register) { inclusive = true }
                } }
            )
        }
        composable(
            route = Routes.Dashboard,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            DashboardScreen(
                userId = userId,
                navController = navController
            )
        }
        composable(
            route = Routes.Home,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            HomeScreen(
                userId = userId,
                onEditClick = { navController.navigate("dashboard/$userId") },
                onInsights = { navController.navigate("insights/$userId") },
                navController = navController
            )
        }
        composable(
            route = Routes.Insights,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val vm: InsightsViewModel = viewModel()
            InsightsScreen(
                userId = userId,
                vm = vm,
                onReturnHome = { navController.navigate("home/$userId") },
                navController = navController
            )
        }
    }
}
