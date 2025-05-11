package com.aaronlamkongyew33521808.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aaronlamkongyew33521808.myapplication.data.entity.NutriCoachTip

@Dao
interface NutriCoachDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: NutriCoachTip)

    @Query("SELECT * FROM nutricoach_tips WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getTipsForUser(userId: String): List<NutriCoachTip>
}
