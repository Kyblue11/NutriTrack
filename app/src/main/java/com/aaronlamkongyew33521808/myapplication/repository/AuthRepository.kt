package com.aaronlamkongyew33521808.myapplication.repository

import com.aaronlamkongyew33521808.myapplication.data.dao.UserDao
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity

class AuthRepository(private val userDao: UserDao) {
    // Step 1: verify pre-registered user by ID+phone
    suspend fun getRawUser(userId: String, phone: String): UserEntity? =
        userDao.getUser(userId, phone)

    // Step 2: claim account (set name + passwordHash)
    suspend fun claimAccount(
        userId: String,
        phone: String,
        name: String,
        passwordHash: String
    ): Boolean {
        val user = userDao.getUser(userId, phone) ?: return false
        val updated = user.copy(name = name, passwordHash = passwordHash)
        userDao.insertUsers(listOf(updated))
        return true
    }

    // login using ID + passwordHash
    suspend fun loginWithPassword(userId: String, passwordHash: String): Boolean {
        val user = userDao.getUserById(userId) ?: return false
        return user.passwordHash == passwordHash
    }

    // For dropdown list in login
    suspend fun getAllUsers(): List<UserEntity> = userDao.getAllUsers()
}