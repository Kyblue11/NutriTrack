package com.aaronlamkongyew33521808.myapplication.auth

    import android.content.Context
    import android.content.Context.MODE_PRIVATE
    import androidx.compose.runtime.State
    import androidx.compose.runtime.mutableStateOf

    object AuthManager {
        val _userId = mutableStateOf<String?>(null)
        val userId: State<String?> = _userId

        // on a successful login
        fun login(userId: String, context: Context) {
            _userId.value = userId
            // also persist to SharedPreferences so we survive process death:
            context.getSharedPreferences("NutriTrackPrefs", MODE_PRIVATE)
                .edit()
                .putString("lastUserId", userId)
                .putBoolean("isLoggedIn", true)
                .apply()
        }

        fun logout(context: Context) {
            _userId.value = null
            context.getSharedPreferences("NutriTrackPrefs", MODE_PRIVATE)
                .edit()
                .remove("lastUserId")
                .putBoolean("isLoggedIn", false)
                .apply()
        }

    }