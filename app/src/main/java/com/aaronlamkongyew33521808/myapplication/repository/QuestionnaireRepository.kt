package com.aaronlamkongyew33521808.myapplication.repository

import com.aaronlamkongyew33521808.myapplication.data.dao.QuestionnaireDao
import com.aaronlamkongyew33521808.myapplication.data.entity.QuestionnaireEntity

class QuestionnaireRepository(private val dao: QuestionnaireDao) {
    suspend fun load(userId: String): QuestionnaireEntity? = dao.getByUserId(userId)
    suspend fun save(entity: QuestionnaireEntity) = dao.insert(entity)
}