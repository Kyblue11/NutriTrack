package com.aaronlamkongyew33521808.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aaronlamkongyew33521808.myapplication.data.dao.QuestionnaireDao
import com.aaronlamkongyew33521808.myapplication.data.dao.UserDao
import com.aaronlamkongyew33521808.myapplication.data.entity.QuestionnaireEntity
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity

@Database(
    entities = [UserEntity::class, QuestionnaireEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questionnaireDao(): QuestionnaireDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nutritrack_db"
                )
                    .fallbackToDestructiveMigration()   //  allow schema reset on version change TODO: is this bad?
                    .build()
                    .also { INSTANCE = it }
            }
    }
}