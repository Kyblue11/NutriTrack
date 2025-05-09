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

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val repo = AuthRepository(AppDatabase.getDatabase(application).userDao())

    // 1) list of userIds for the dropdown
    private val _userIds = MutableStateFlow<List<String>>(emptyList())
    val userIds: StateFlow<List<String>> get() = _userIds

    // 2) login result
    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess: StateFlow<Boolean?> get() = _loginSuccess

    init {
        viewModelScope.launch {
            val users = repo.getAllUsers()
            _userIds.value = users.map { it.userId }
            Log.d("LoginVM", "Loaded ${_userIds.value.size} users: ${_userIds.value}")
        }
    }


    fun login(userId: String, phone: String) = viewModelScope.launch {
        _loginSuccess.value = repo.login(userId, phone) != null
    }
}