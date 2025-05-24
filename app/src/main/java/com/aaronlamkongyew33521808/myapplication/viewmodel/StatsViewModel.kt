package com.aaronlamkongyew33521808.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.aaronlamkongyew33521808.myapplication.repository.StatsRepository

class StatsViewModel(
    private val repo: StatsRepository
): ViewModel() {
    val allUsersTotalScore = liveData {
        emit(repo.getAllUsersTotalScores())
    }
    val allUsersSubScores = liveData {
        emit(repo.getAllUsersSubScores())
    }
}
