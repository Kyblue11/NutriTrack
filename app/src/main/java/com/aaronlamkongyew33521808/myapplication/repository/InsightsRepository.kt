package com.aaronlamkongyew33521808.myapplication.repository

import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity

class InsightsRepository(private val db: AppDatabase) {
    suspend fun getTotalScore(userId: String): Double {
        val user = db.userDao().getUserById(userId) ?: return 0.0
        return if (user.sex == "Male") user.HEIFAtotalscoreMale else user.HEIFAtotalscoreFemale
    }

    suspend fun getSubScores(userId: String): Map<String, Double> {
        val user = db.userDao().getUserById(userId) ?: return emptyMap()
        val male = user.sex == "Male"
        return mapOf(
            "Vegetables" to if (male) user.vegetablesHEIFAscoreMale else user.vegetablesHEIFAscoreFemale,
            "Fruits" to if (male) user.fruitHEIFAscoreMale else user.fruitHEIFAscoreFemale,
            "Grains & Cereals" to if (male) user.grainsAndCerealsHEIFAscoreMale else user.grainsAndCerealsHEIFAscoreFemale,
            "Whole Grains" to if (male) user.wholegrainsHEIFAscoreMale else user.wholegrainsHEIFAscoreFemale,
            "Meat & Alternatives" to if (male) user.meatAndAlternativesHEIFAscoreMale else user.meatAndAlternativesHEIFAscoreFemale,
            "Dairy" to if (male) user.dairyAndAlternativesHEIFAscoreMale else user.dairyAndAlternativesHEIFAscoreFemale,
            "Water" to if (male) user.waterHEIFAscoreMale else user.waterHEIFAscoreFemale,
            "Saturated Fats" to if (male) user.SaturatedFatHEIFAscoreMale else user.SaturatedFatHEIFAscoreFemale,
            "Unsaturated Fats" to if (male) user.unsaturatedFatHEIFAscoreMale else user.unsaturatedFatHEIFAscoreFemale,
            "Sodium" to if (male) user.sodiumHEIFAscoreMale else user.sodiumHEIFAscoreFemale,
            "Sugar" to if (male) user.sugarHEIFAscoreMale else user.sugarHEIFAscoreFemale,
            "Alcohol" to if (male) user.alcoholHEIFAscoreMale else user.alcoholHEIFAscoreFemale,
            "Discretionary Foods" to if (male) user.discretionaryHEIFAscoreMale else user.discretionaryHEIFAscoreFemale
        )
    }
}