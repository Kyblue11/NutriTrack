package com.aaronlamkongyew33521808.myapplication.repository

import com.aaronlamkongyew33521808.myapplication.data.dao.UserDao
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity

class AuthRepository(private val userDao: UserDao) {
    suspend fun login(userId: String, phone: String) =
        userDao.getUser(userId, phone)

    suspend fun register(entity: UserEntity) {
        userDao.insertUsers(listOf(entity))
    }
}