package com.aaronlamkongyew33521808.myapplication.auth

    import android.content.Context
    import android.content.Context.MODE_PRIVATE
    import androidx.compose.runtime.State
    import androidx.compose.runtime.mutableStateOf

    object AuthManager {
        // Compose-friendly mutable state so that any @Composable observing this will recompose
        public val _userId = mutableStateOf<String?>(null) // TODO: Make this private, but conflict with MainActivity?
        val userId: State<String?> = _userId

        // Call this on a successful login
        fun login(userId: String, context: Context) {
            _userId.value = userId
            // also persist to SharedPreferences so we survive process death:
            context.getSharedPreferences("NutriTrackPrefs", MODE_PRIVATE)
                .edit()
                .putString("lastUserId", userId)
                .putBoolean("isLoggedIn", true)
                .apply()
        }

        // Call this on logout
        fun logout(context: Context) {
            _userId.value = null
            context.getSharedPreferences("NutriTrackPrefs", MODE_PRIVATE)
                .edit()
                .remove("lastUserId")
                .putBoolean("isLoggedIn", false)
                .apply()
        }

        fun getStudentId(): String? = _userId.value
    }