package com.aaronlamkongyew33521808.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AuthRepository(AppDatabase.getDatabase(application).userDao())

    private val _userIds = MutableStateFlow<List<String>>(emptyList())
    val userIds: StateFlow<List<String>> get() = _userIds

    private val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult: StateFlow<Boolean?> get() = _loginResult

    // pre-loads all the user IDs automatically when this ViewModel is created for the UI composable
    init {
        viewModelScope.launch {
            // only users who have both name & passwordHash non-null
            _userIds.value = repo.getAllUsers()
                .filter { it.name != null && it.passwordHash != null }
                .map { it.userId }
        }
    }

    fun login(userId: String, password: String) = viewModelScope.launch {
        val hash = password.sha256()
        _loginResult.value = repo.loginWithPassword(userId, hash)
    }

    private fun String.sha256(): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(this.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }
}