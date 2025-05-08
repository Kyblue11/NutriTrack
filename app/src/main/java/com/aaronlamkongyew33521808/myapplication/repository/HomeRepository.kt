package com.aaronlamkongyew33521808.myapplication.repository

import com.aaronlamkongyew33521808.myapplication.data.dao.UserDao
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity

class HomeRepository(private val dao: UserDao) {
    suspend fun getUserById(id: String): UserEntity? = dao.getUserById(id)
}