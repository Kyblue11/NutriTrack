package com.aaronlamkongyew33521808.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutricoach_tips")
data class NutriCoachTip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val tip: String,
    val timestamp: Long = System.currentTimeMillis()
)