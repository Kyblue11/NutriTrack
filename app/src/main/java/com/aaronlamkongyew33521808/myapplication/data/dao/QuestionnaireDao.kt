package com.aaronlamkongyew33521808.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aaronlamkongyew33521808.myapplication.data.entity.QuestionnaireEntity

@Dao
interface QuestionnaireDao {
    @Query("SELECT * FROM questionnaires WHERE userId = :id")
    suspend fun getByUserId(id: String): QuestionnaireEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QuestionnaireEntity)
}