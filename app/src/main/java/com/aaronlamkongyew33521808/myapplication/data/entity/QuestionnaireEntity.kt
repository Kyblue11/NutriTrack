package com.aaronlamkongyew33521808.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questionnaires")
data class QuestionnaireEntity(
    @PrimaryKey val userId: String,
    val fruits: Boolean = false,
    val vegetables: Boolean = false,
    val grains: Boolean = false,
    val redMeat: Boolean = false,
    val seafood: Boolean = false,
    val poultry: Boolean = false,
    val fish: Boolean = false,
    val eggs: Boolean = false,
    val nutsSeeds: Boolean = false,
    val persona: String = "",
    val biggestMealTime: String = "",
    val sleepTime: String = "",
    val wakeTime: String = ""
)
