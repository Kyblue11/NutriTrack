package com.aaronlamkongyew33521808.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aaronlamkongyew33521808.myapplication.data.entity.ClinicianKeyEntity

@Dao
interface ClinicianKeyDao {
    @Query("SELECT `key` FROM clinician_keys WHERE id = 0 LIMIT 1")
    suspend fun getKey(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ClinicianKeyEntity)
}