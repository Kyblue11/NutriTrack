package com.aaronlamkongyew33521808.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity
import com.aaronlamkongyew33521808.myapplication.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AuthRepository(AppDatabase.getDatabase(application).userDao())

    //  all pre-registered IDs for dropdown
    private val _userIds = MutableStateFlow<List<String>>(emptyList())
    val userIds: StateFlow<List<String>> get() = _userIds

    // can the user claim this ID+phone?
    private val _canClaim = MutableStateFlow<Boolean?>(null)
    val canClaim: StateFlow<Boolean?> get() = _canClaim

    // result of final claim (name/password)
    private val _claimResult = MutableStateFlow<Boolean?>(null)
    val claimResult: StateFlow<Boolean?> get() = _claimResult

    init {
        viewModelScope.launch {
            val users = repo.getAllUsers()
                .filter { it.name == null && it.passwordHash == null }
            _userIds.value = users.map { it.userId }
        }
    }

    fun resetCanClaim() {
        _canClaim.value = null
    }

    fun verifyIdPhone(userId: String, phone: String) = viewModelScope.launch {
        _canClaim.value = repo.getRawUser(userId, phone) != null
    }

    fun claimAccount(userId: String, phone: String, name: String, password: String) = viewModelScope.launch {
        val hash = password.sha256()
        _claimResult.value = repo.claimAccount(userId, phone, name, hash)
    }

    private fun String.sha256(): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(this.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}
