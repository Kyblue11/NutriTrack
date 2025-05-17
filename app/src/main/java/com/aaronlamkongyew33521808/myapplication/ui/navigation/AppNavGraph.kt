package com.aaronlamkongyew33521808.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aaronlamkongyew33521808.myapplication.auth.AuthManager
import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.data.api.FruityViceApi
import com.aaronlamkongyew33521808.myapplication.data.api.buildAPI
import com.aaronlamkongyew33521808.myapplication.repository.HomeRepository
import com.aaronlamkongyew33521808.myapplication.repository.NutriCoachRepository
import com.aaronlamkongyew33521808.myapplication.repository.QuestionnaireRepository
import com.aaronlamkongyew33521808.myapplication.ui.clinician.ClinicianDashboardScreen
import com.aaronlamkongyew33521808.myapplication.ui.dashboard.DashboardScreen
import com.aaronlamkongyew33521808.myapplication.ui.home.HomeScreen
import com.aaronlamkongyew33521808.myapplication.ui.insights.InsightsScreen
import com.aaronlamkongyew33521808.myapplication.ui.login.LoginScreen
import com.aaronlamkongyew33521808.myapplication.ui.nutricoach.NutriCoachScreen
import com.aaronlamkongyew33521808.myapplication.ui.register.RegisterScreen
import com.aaronlamkongyew33521808.myapplication.ui.settings.SettingsScreen
import com.aaronlamkongyew33521808.myapplication.ui.welcome.WelcomeScreen
import com.aaronlamkongyew33521808.myapplication.viewmodel.ClinicianViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.InsightsViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.LoginViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.NutriCoachViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.NutriCoachViewModelFactory
import com.aaronlamkongyew33521808.myapplication.viewmodel.RegisterViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.SettingsViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Routes {
    const val Welcome = "welcome"
    const val Login = "login"
    const val Register = "register"
    const val Dashboard = "dashboard/{userId}"
    const val Home = "home/{userId}"
    const val Insights = "insights/{userId}"
    const val NutriCoach = "coach/{userId}"
    const val Settings = "settings/{userId}"
    const val ClinicianDashboard = "clinician_dashboard"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val currentUser   by AuthManager.userId
    // if there's a user, skip straight in; otherwise show welcome/login
    val startDestination = if (currentUser != null) {
        "dashboard/$currentUser"
    } else {
        Routes.Welcome
    }


    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.Welcome) {
            WelcomeScreen {
                navController.navigate(Routes.Login) {
                }
            }
        }

        composable(Routes.Login) {
            val vm: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = vm,
                onLoginSuccess = { id ->
                    navController.navigate("dashboard/$id")
                },
                onRegister = { navController.navigate(Routes.Register) }
            )
        }

        composable(Routes.Register) {
            val vm: RegisterViewModel = viewModel()
            RegisterScreen(
                viewModel = vm,
                onDone = {
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Register) { inclusive = true }
                    }
                }
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
                onInsights = {
                    navController.navigate("insights/$userId")
                    {
                        popUpTo(Routes.Home) {
                            inclusive = true
                        } // needed to prevent bug [Insights Screen to Home Screen via bottom bar]
                    }
                },
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
                onReturnHome = {
                    navController.navigate("home/$userId")
                    {
                        popUpTo(Routes.Insights) {
                            inclusive = true
                        } // needed to prevent bug [Home Screen to Insights Screen via bottom bar]
                    }

                },
                onImproveClick = {
                    navController.navigate("coach/$userId")
                    {
                        popUpTo(Routes.Insights) {
                            inclusive = true
                        }
                    }
                },
                navController = navController
            )
        }

        composable(
            Routes.NutriCoach, arguments = listOf(
                navArgument("userId")
                { type = NavType.StringType }
            )
        )
        { back ->
            val userId = back.arguments!!.getString("userId")!!
            val context = LocalContext.current

            val db = AppDatabase.getDatabase(context)
            val dao = db.nutriCoachDao()
            val repo = NutriCoachRepository(buildAPI.fruityApi, dao)
            val homeRepo = HomeRepository(
                db.userDao()
            )
            val quesRepo = QuestionnaireRepository(
                db.questionnaireDao()
            )

            val vm: NutriCoachViewModel = viewModel(
                factory = NutriCoachViewModelFactory(
                    repo,
                    homeRepo,
                    quesRepo
                )
            )
            NutriCoachScreen(
                userId,
                viewModel = vm,
                navController = navController,
                onReturnHome = {
                    navController.navigate("home/$userId") {
                        popUpTo(Routes.NutriCoach) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = Routes.Settings,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            val vm: SettingsViewModel = viewModel(
                factory = SettingsViewModel.Factory(
                    AppDatabase.getDatabase(LocalContext.current).userDao(), userId
                )
            )
            SettingsScreen(
                viewModel = vm,
                onLogout = {
                    // clear any session if you have one, then:
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Welcome) { inclusive = false }
                    }
                },
                onClinician = {
                    navController.navigate(Routes.ClinicianDashboard)
                },
                navController = navController,
                onReturnHome = {
                    navController.navigate("home/$userId") {
                        popUpTo(Routes.Settings) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.ClinicianDashboard) {
            ClinicianDashboardScreen(
                onDone = { navController.popBackStack() }
            )
        }
    }
}