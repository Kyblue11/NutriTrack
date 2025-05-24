package com.aaronlamkongyew33521808.myapplication.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
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
import com.aaronlamkongyew33521808.myapplication.repository.ClinicianKeyRepository
import com.aaronlamkongyew33521808.myapplication.repository.HomeRepository
import com.aaronlamkongyew33521808.myapplication.repository.NutriCoachRepository
import com.aaronlamkongyew33521808.myapplication.repository.QuestionnaireRepository
import com.aaronlamkongyew33521808.myapplication.repository.StatsRepository
import com.aaronlamkongyew33521808.myapplication.ui.charts.CombinedStatsScreen
import com.aaronlamkongyew33521808.myapplication.ui.clinician.ClinicianDashboardScreen
import com.aaronlamkongyew33521808.myapplication.ui.dashboard.DashboardScreen
import com.aaronlamkongyew33521808.myapplication.ui.home.HomeScreen
import com.aaronlamkongyew33521808.myapplication.ui.insights.InsightsScreen
import com.aaronlamkongyew33521808.myapplication.ui.login.LoginScreen
import com.aaronlamkongyew33521808.myapplication.ui.nutricoach.NutriCoachScreen
import com.aaronlamkongyew33521808.myapplication.ui.register.RegisterScreen
import com.aaronlamkongyew33521808.myapplication.ui.settings.SettingsScreen
import com.aaronlamkongyew33521808.myapplication.ui.welcome.WelcomeScreen
import com.aaronlamkongyew33521808.myapplication.viewmodel.ClinicianKeyViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.ClinicianKeyViewModelFactory
import com.aaronlamkongyew33521808.myapplication.viewmodel.ClinicianViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.InsightsViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.LoginViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.NutriCoachViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.NutriCoachViewModelFactory
import com.aaronlamkongyew33521808.myapplication.viewmodel.RegisterViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.SettingsViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.StatsViewModel
import com.aaronlamkongyew33521808.myapplication.viewmodel.StatsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Routes {
    const val Launcher = "launcher"
    const val Welcome = "welcome"
    const val Login = "login"
    const val Register = "register"
    const val Dashboard = "dashboard/{userId}"
    const val Home = "home/{userId}"
    const val Insights = "insights/{userId}"
    const val NutriCoach = "coach/{userId}"
    const val Settings = "settings/{userId}"
    const val ClinicianDashboard = "clinician_dashboard/{userId}"
    const val StatsCombined = "stats/combined/{userId}"
}

@Composable
fun AppNavGraph() {
    val context       = LocalContext.current
    val prefs         = context.getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
    val lastRoutePref = prefs.getString("lastRoute", null)

    val navController = rememberNavController()

    // 1. read current user once
    val currentUser by AuthManager.userId

DrawerLayout(navController = navController, userId = currentUser) { openDrawer ->
    NavHost(navController, startDestination = Routes.Launcher) {

        // 1) The “launcher” placeholder
        composable(Routes.Launcher) {
            // 1a) Check questionnaire filled
            val hasFilled by produceState<Boolean?>(initialValue = null, currentUser) {
                value = if (currentUser == null) {
                    false
                } else {
                    withContext(Dispatchers.IO) {
                        AppDatabase.getDatabase(context)
                            .questionnaireDao()
                            .getByUserId(currentUser!!)!= null
                    }
                }
            }

    // until we know `hasFilled` is true, we don't navigate anywhere
    LaunchedEffect(currentUser, hasFilled) {
        if (hasFilled == null) return@LaunchedEffect

                when {
                    currentUser == null ->
                        navController.navigate(Routes.Welcome) {
                            popUpTo(Routes.Launcher) { inclusive = true }
                        }

                    hasFilled == false ->
                        navController.navigate(Routes.Dashboard.replace("{userId}", currentUser!!)) {
                            popUpTo(Routes.Launcher) { inclusive = true }
                        }

                    // logged in & questionnaire done
                    else   -> {
                        val start = lastRoutePref
                            ?.takeIf { it.startsWith("home/") ||
                                    it.startsWith("insights/") ||
                                    it.startsWith("coach/") ||
                                    it.startsWith("settings/") }
                            ?: Routes.Home.replace("{userId}", currentUser!!)

                        navController.navigate(start) {
                            popUpTo(Routes.Launcher) { inclusive = true }
                        }
                    }
                }
            }
        }


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
                        // clear out any stale lastRoute
                        context.getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
                            .edit()
                            .remove("lastRoute")
                            .apply()
                        // update AuthManager
                        AuthManager.login(id, context)
                        // go via launcher so that my hasFilled logic fires up
                        navController.navigate(Routes.Launcher) {
                            popUpTo(Routes.Welcome) { inclusive = true }
                        }
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
                    onMenuClick = openDrawer,
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
                val keyVm: ClinicianKeyViewModel = viewModel(
                    factory = ClinicianKeyViewModelFactory(
                        ClinicianKeyRepository(AppDatabase.getDatabase(LocalContext.current))
                    )
                )
                SettingsScreen(
                    viewModel = vm,
                    keyViewModel = keyVm,
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
                val statsViewModel: StatsViewModel = viewModel(
                    factory = StatsViewModelFactory(
                        StatsRepository(AppDatabase.getDatabase(LocalContext.current))
                    )
                )
                ClinicianDashboardScreen(
                    statsViewModel,
                    onDone = { navController.popBackStack() },
                    onReturnHome = {navController.popBackStack()
                    }
                )
            }

            composable(Routes.StatsCombined,
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId").orEmpty()
                val vm: StatsViewModel = viewModel(
                    factory = StatsViewModelFactory(
                        StatsRepository(AppDatabase.getDatabase(LocalContext.current))
                    )
                )
                val keyVm: ClinicianKeyViewModel = viewModel(
                    factory = ClinicianKeyViewModelFactory(
                        ClinicianKeyRepository(AppDatabase.getDatabase(LocalContext.current))
                    )
                )
                val insightsvm: InsightsViewModel = viewModel()
                CombinedStatsScreen(
                    vm,
                    keyVm,
                    insightsvm,
                    onMenuClick = openDrawer,
                    userId = userId
                )
            }
        }
    }
}