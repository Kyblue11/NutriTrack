package com.aaronlamkongyew33521808.myapplication.repository

import com.aaronlamkongyew33521808.myapplication.data.AppDatabase

class StatsRepository(private val db: AppDatabase) {
    suspend fun getAllUsersTotalScores(): List<Pair<String, Double>> =
        db.userDao().getAllUsers().map { it.userId to it.HEIFAtotalscore }

    suspend fun getAllUsersSubScores(): List<UserSubScore> =
        db.userDao().getAllUsers().map { UserSubScore(it.userId, it.fruitHEIFAscore, it.vegetablesHEIFAscore) }
}

data class UserSubScore(val userId: String, val fruitScore: Double, val vegetableScore: Double)
