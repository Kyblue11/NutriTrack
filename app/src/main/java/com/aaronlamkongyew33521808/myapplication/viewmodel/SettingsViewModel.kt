package com.aaronlamkongyew33521808.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aaronlamkongyew33521808.myapplication.data.dao.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

class SettingsViewModel(
    private val dao: UserDao,
    private val userId: String
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _phone = MutableStateFlow("")
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

    suspend fun updateProfile(
        newName: String,
        newPhone: String
    ): Boolean {

        val user = dao.getUserById(userId) ?: return false

        //  Build updated entity and save
        val updated = user.copy(
            name         = newName,
            phoneNumber  = newPhone
        )
        dao.insertUsers(listOf(updated))

        //  Refresh state flows
        _name.value  = updated.name ?: updated.userId
        _phone.value = updated.phoneNumber

        return true
    }

    //  Returns true if update succeeded (password matched), false otherwise
    suspend fun updateProfile(
        currentPass: String,
        newName:     String,
        newPhone:    String,
        newPass:     String
    ): Boolean {
        val user = dao.getUserById(userId) ?: return false
        //  Validate current password
        if (user.passwordHash != currentPass.sha256()) return false

        //  Determine new password hash (keep old if newPass blank)
        val newHash = if (newPass.isNotBlank()) newPass.sha256() else user.passwordHash

        //  Build updated entity and save
        val updated = user.copy(
            name         = newName,
            phoneNumber  = newPhone,
            passwordHash = newHash
        )
        dao.insertUsers(listOf(updated))

        // refresh state flows
        _name.value  = updated.name ?: updated.userId
        _phone.value = updated.phoneNumber

        return true
    }

    // SHA-256 helper
    private fun String.sha256(): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(toByteArray())
            .joinToString("") { "%02x".format(it) }
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