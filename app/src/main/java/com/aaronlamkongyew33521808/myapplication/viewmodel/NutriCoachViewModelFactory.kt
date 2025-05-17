package com.aaronlamkongyew33521808.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aaronlamkongyew33521808.myapplication.repository.HomeRepository
import com.aaronlamkongyew33521808.myapplication.repository.NutriCoachRepository
import com.aaronlamkongyew33521808.myapplication.repository.QuestionnaireRepository

class NutriCoachViewModelFactory(
    private val repo: NutriCoachRepository,
    private val homeRepo: HomeRepository,
    private val quesRepo: QuestionnaireRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutriCoachViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NutriCoachViewModel(repo, homeRepo, quesRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}