package com.aaronlamkongyew33521808.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aaronlamkongyew33521808.myapplication.data.dao.ClinicianKeyDao
import com.aaronlamkongyew33521808.myapplication.data.dao.NutriCoachDao
import com.aaronlamkongyew33521808.myapplication.data.dao.QuestionnaireDao
import com.aaronlamkongyew33521808.myapplication.data.dao.UserDao
import com.aaronlamkongyew33521808.myapplication.data.entity.ClinicianKeyEntity
import com.aaronlamkongyew33521808.myapplication.data.entity.NutriCoachTip
import com.aaronlamkongyew33521808.myapplication.data.entity.QuestionnaireEntity
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity

@Database(
    entities = [UserEntity::class, QuestionnaireEntity::class, NutriCoachTip::class, ClinicianKeyEntity::class],
    version = 12,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questionnaireDao(): QuestionnaireDao
    abstract fun nutriCoachDao(): NutriCoachDao
    abstract fun clinicianKeyDao(): ClinicianKeyDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nutritrack_db"
                )
                    .fallbackToDestructiveMigration()   //  allow schema reset on version change
                    .build()
                    .also { INSTANCE = it }
            }
    }
}