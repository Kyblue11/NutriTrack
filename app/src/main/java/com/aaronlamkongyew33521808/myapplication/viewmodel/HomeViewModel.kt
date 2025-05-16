package com.aaronlamkongyew33521808.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//In this case, using AndroidViewModel(application: Application) is appropriate and simple because:
//The only dependency needed is the Application context, which is used to get the database singleton.
//No other dependency injections (like repositories or DAOs) are injected from outside.
//You do not need to pass custom parameters to the ViewModel.
//Factory pattern is better if you want to inject dependencies (e.g., repositories, DAOs, or other objects) for better testability and separation of concerns.

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = HomeRepository(AppDatabase.getDatabase(application).userDao())

    private val _userId = MutableStateFlow("Guest")
    val userId = _userId.asStateFlow()

    private val _userName = MutableStateFlow("Guest")
    val userName = _userName.asStateFlow()

    private val _foodQualityScore = MutableStateFlow(0.0)
    val foodQualityScore = _foodQualityScore.asStateFlow()

    fun load(userId: String) = viewModelScope.launch {
        val user = repo.getUserById(userId)
        _userId.value = user?.userId ?: "Guest"
        _userName.value = user?.name ?: "Guest"
        _foodQualityScore.value =
            user?.HEIFAtotalscore ?: 0.0 // TODO: test this placeholder (maybe replace with average of both scores)

    }
}