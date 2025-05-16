package com.aaronlamkongyew33521808.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aaronlamkongyew33521808.myapplication.repository.HomeRepository
import com.aaronlamkongyew33521808.myapplication.repository.NutriCoachRepository

class NutriCoachViewModelFactory(
    private val repo: NutriCoachRepository,
    private val homeRepo: HomeRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutriCoachViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NutriCoachViewModel(repo, homeRepo, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class") // TODO: is this bad practice?
    }
}