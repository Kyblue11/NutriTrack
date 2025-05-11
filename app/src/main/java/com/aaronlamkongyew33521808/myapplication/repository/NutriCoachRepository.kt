package com.aaronlamkongyew33521808.myapplication.repository

import com.aaronlamkongyew33521808.myapplication.data.api.Fruit
import com.aaronlamkongyew33521808.myapplication.data.api.FruityViceApi
import com.aaronlamkongyew33521808.myapplication.data.dao.NutriCoachDao
import com.aaronlamkongyew33521808.myapplication.data.entity.NutriCoachTip

class NutriCoachRepository(
    private val fruitApi: FruityViceApi,
    private val dao: NutriCoachDao
) {
    suspend fun fetchFruits(): List<Fruit> = fruitApi.getAllFruits()
    suspend fun saveTip(tip: NutriCoachTip) = dao.insertTip(tip)
    suspend fun getTips(userId: String): List<NutriCoachTip> = dao.getTipsForUser(userId)
}