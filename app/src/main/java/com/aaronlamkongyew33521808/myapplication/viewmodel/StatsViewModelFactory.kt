package com.aaronlamkongyew33521808.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aaronlamkongyew33521808.myapplication.repository.StatsRepository

class StatsViewModelFactory(
    private val repo: StatsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        StatsViewModel(repo) as T
}
