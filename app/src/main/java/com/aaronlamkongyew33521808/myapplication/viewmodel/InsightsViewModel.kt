package com.aaronlamkongyew33521808.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aaronlamkongyew33521808.myapplication.repository.InsightsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InsightsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = InsightsRepository(
        com.aaronlamkongyew33521808.myapplication.data.AppDatabase
            .getDatabase(application)
    )

    private val _totalScore = MutableStateFlow(0.0)
    val totalScore: StateFlow<Double> = _totalScore.asStateFlow()

    private val _subScores = MutableStateFlow<Map<String, Double>>(emptyMap())
    val subScores: StateFlow<Map<String, Double>> = _subScores.asStateFlow()

    fun load(userId: String) {
        viewModelScope.launch {
            _totalScore.value = repo.getTotalScore(userId)
            _subScores.value = repo.getSubScores(userId)
        }
    }
}