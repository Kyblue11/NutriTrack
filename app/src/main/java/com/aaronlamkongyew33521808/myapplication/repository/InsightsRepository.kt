package com.aaronlamkongyew33521808.myapplication.repository

import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity

class InsightsRepository(private val db: AppDatabase) {
    suspend fun getTotalScore(userId: String): Double {
        val user = db.userDao().getUserById(userId) ?: return 0.0
        return user.HEIFAtotalscore
    }

    suspend fun getSubScores(userId: String): Map<String, Double> {
        val user = db.userDao().getUserById(userId) ?: return emptyMap()
        return mapOf(
            "Vegetables" to user.vegetablesHEIFAscore,
            "Fruits" to user.fruitHEIFAscore,
            "Grains & Cereals" to user.grainsAndCerealsHEIFAscore,
            "Whole Grains" to user.wholegrainsHEIFAscore,
            "Meat & Alternatives" to user.meatAndAlternativesHEIFAscore,
            "Dairy" to user.dairyAndAlternativesHEIFAscore,
            "Water" to user.waterHEIFAscore,
            "Saturated Fats" to user.SaturatedFatHEIFAscore,
            "Unsaturated Fats" to user.unsaturatedFatHEIFAscore,
            "Sodium" to user.sodiumHEIFAscore,
            "Sugar" to user.sugarHEIFAscore,
            "Alcohol" to user.alcoholHEIFAscore,
            "Discretionary Foods" to user.discretionaryHEIFAscore,
        )
    }
}