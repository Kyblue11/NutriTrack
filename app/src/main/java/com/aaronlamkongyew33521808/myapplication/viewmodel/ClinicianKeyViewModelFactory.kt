package com.aaronlamkongyew33521808.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aaronlamkongyew33521808.myapplication.repository.ClinicianKeyRepository

class ClinicianKeyViewModelFactory(
    private val repo: ClinicianKeyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClinicianKeyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClinicianKeyViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown model class ${modelClass.name}")
    }
}