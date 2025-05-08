package com.aaronlamkongyew33521808.myapplication

import android.app.Application
import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NutriTrackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Room database and prepopulate from CSV on first launch
        val db = AppDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            val dao = db.userDao()
            if (dao.countUsers() == 0) {
                val list = loadCSV(this@NutriTrackApp)
                dao.insertUsers(list.map { UserEntity.fromUserData(it) })
            }
        }
    }
}