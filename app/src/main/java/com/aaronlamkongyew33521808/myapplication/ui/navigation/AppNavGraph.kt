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
import com.aaronlamkongyew33521808.myapplication.viewmodel.LoginViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.RegisterViewModel

object Routes {
    const val Login = "login"
    const val Register = "register"
    const val Dashboard = "dashboard/{userId}" // pass userId
    const val Home = "home/{userId}"
    const val Insights = "insights/{userId}"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Login) {
        composable(Routes.Login) {
            val vm: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = vm,
                onLoginSuccess = { id -> navController.navigate("dashboard/$id") },
                onRegister = { navController.navigate(Routes.Register) }
            )
        }
        composable(Routes.Register) {
            val vm: RegisterViewModel = viewModel()
            RegisterScreen(
                viewModel = vm,
                onRegisterSuccess = { id -> navController.popBackStack(); navController.navigate("login?userId=$id") }
            )
        }
        composable(
            "dashboard/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStack ->
            val userId = backStack.arguments?.getString("userId") ?: ""
            DashboardScreen(userId = userId, onContinue = { navController.navigate("home/$userId") })
        }
        composable(
            "home/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStack ->
            val userId = backStack.arguments?.getString("userId") ?: ""
            HomeScreen(userId = userId, onInsights = { navController.navigate("insights/$userId") })
        }
        composable(
            "insights/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStack ->
            val userId = backStack.arguments?.getString("userId") ?: ""
            InsightsScreen(userId = userId)
        }
    }
}
