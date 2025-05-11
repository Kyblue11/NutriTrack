package com.aaronlamkongyew33521808.myapplication.data.api

data class Fruit(
    val name: String,
    val family : String,
    val nutritions: Nutrition
)

data class Nutrition(
    val calories: Double,
    val fat: Double,
    val sugar: Double,
    val carbohydrates: Double,
    val protein: Double,
)
