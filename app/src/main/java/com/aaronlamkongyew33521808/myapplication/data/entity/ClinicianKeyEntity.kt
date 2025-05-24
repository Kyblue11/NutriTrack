package com.aaronlamkongyew33521808.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clinician_keys")
data class ClinicianKeyEntity(
    @PrimaryKey val id: Int = 0,
    val key: String
)
