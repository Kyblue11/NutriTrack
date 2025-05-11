package com.aaronlamkongyew33521808.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aaronlamkongyew33521808.myapplication.data.dao.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dao: UserDao,
    private val userId: String
) : ViewModel() {

    private val _name = MutableStateFlow("-")
    val name: StateFlow<String> = _name

    private val _phone = MutableStateFlow("-")
    val phone: StateFlow<String> = _phone

    private val _id = MutableStateFlow(userId)
    val id: StateFlow<String> = _id

    init {
        viewModelScope.launch {
            dao.getUserById(userId)?.let { user ->
                _name.value  = user.name ?: user.userId
                _phone.value = user.phoneNumber
            }
        }
    }

    class Factory(
        private val dao: UserDao,
        private val userId: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(dao, userId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}