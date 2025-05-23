package com.aaronlamkongyew33521808.myapplication.repository

import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.data.entity.ClinicianKeyEntity

class ClinicianKeyRepository(private val db: AppDatabase) {
    private val dao = db.clinicianKeyDao()

    suspend fun getKey(): String? = dao.getKey()

    suspend fun saveKey(key: String) = dao.insert(ClinicianKeyEntity(key = key))
}