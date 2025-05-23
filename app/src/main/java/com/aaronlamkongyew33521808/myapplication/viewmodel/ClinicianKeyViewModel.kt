package com.aaronlamkongyew33521808.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.aaronlamkongyew33521808.myapplication.repository.ClinicianKeyRepository

class ClinicianKeyViewModel(
    private val repo: ClinicianKeyRepository
) : ViewModel() {
    private val _key = MutableStateFlow<String?>(null)
    val key: StateFlow<String?> = _key

    init {
        viewModelScope.launch {
            // load existing or set default
            val existing = repo.getKey()
            if (existing != null) {
                _key.value = existing
            } else {
                val default = "dollar-entry-apples"
                repo.saveKey(default)
                _key.value = default
            }
        }
    }
}