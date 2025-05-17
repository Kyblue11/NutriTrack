package com.aaronlamkongyew33521808.myapplication

import android.app.Application
import android.content.Context
import android.util.Log
import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.data.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class NutriTrackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Room database and prepopulate from CSV on first launch
        val db = AppDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            val dao = db.userDao()
            if (dao.countUsers() == 0) { // Only load CSV if no users exist in the database
                val list = loadCSV(this@NutriTrackApp)
                dao.insertUsers(list.map { UserEntity.fromUserData(it) })
            }
        }
    }
}

fun loadCSV(context: Context): List<UserData> {
    val userList = mutableListOf<UserData>()
    try {
        val inputStream = context.assets.open("participantData.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val allLines = reader.readLines()
        if (allLines.isNotEmpty()) {
            val header = allLines.first().split(",")
            val colIdx = header.mapIndexed { idx, name -> name.trim() to idx }.toMap()
            allLines.drop(1).forEach { line ->
                val tokens = line.split(",")
                val sex = tokens[colIdx["Sex"] ?: error("Sex column missing")].trim()
                val isMale = sex == "Male"

                fun get(col: String) = tokens[colIdx[col] ?: error("$col column missing")].toDoubleOrNull() ?: 0.0

                userList.add(
                    UserData(
                        phoneNumber = tokens[colIdx["PhoneNumber"] ?: error("PhoneNumber column missing")].trim().removePrefix("\""),
                        userId = tokens[colIdx["User_ID"] ?: error("User_ID column missing")].trim(),
                        sex = sex,
                        HEIFAtotalscore = get(if (isMale) "HEIFAtotalscoreMale" else "HEIFAtotalscoreFemale"),
                        discretionaryHEIFAscore = get(if (isMale) "DiscretionaryHEIFAscoreMale" else "DiscretionaryHEIFAscoreFemale"),
                        vegetablesHEIFAscore = get(if (isMale) "VegetablesHEIFAscoreMale" else "VegetablesHEIFAscoreFemale"),
                        fruitHEIFAscore = get(if (isMale) "FruitHEIFAscoreMale" else "FruitHEIFAscoreFemale"),
                        fruitServeSize = get("Fruitservesize"),
                        fruitVariationScore = get("Fruitvariationsscore"),
                        grainsAndCerealsHEIFAscore = get(if (isMale) "GrainsandcerealsHEIFAscoreMale" else "GrainsandcerealsHEIFAscoreFemale"),
                        wholegrainsHEIFAscore = get(if (isMale) "WholegrainsHEIFAscoreMale" else "WholegrainsHEIFAscoreFemale"),
                        meatAndAlternativesHEIFAscore = get(if (isMale) "MeatandalternativesHEIFAscoreMale" else "MeatandalternativesHEIFAscoreFemale"),
                        dairyAndAlternativesHEIFAscore = get(if (isMale) "DairyandalternativesHEIFAscoreMale" else "DairyandalternativesHEIFAscoreFemale"),
                        sodiumHEIFAscore = get(if (isMale) "SodiumHEIFAscoreMale" else "SodiumHEIFAscoreFemale"),
                        alcoholHEIFAscore = get(if (isMale) "AlcoholHEIFAscoreMale" else "AlcoholHEIFAscoreFemale"),
                        waterHEIFAscore = get(if (isMale) "WaterHEIFAscoreMale" else "WaterHEIFAscoreFemale"),
                        sugarHEIFAscore = get(if (isMale) "SugarHEIFAscoreMale" else "SugarHEIFAscoreFemale"),
                        SaturatedFatHEIFAscore = get(if (isMale) "SaturatedFatHEIFAscoreMale" else "SaturatedFatHEIFAscoreFemale"),
                        unsaturatedFatHEIFAscore = get(if (isMale) "UnsaturatedFatHEIFAscoreMale" else "UnsaturatedFatHEIFAscoreFemale")
                    )
                )
            }
        }
    } catch (e: Exception) {
        Log.e("CSV_DEBUG", "Error reading CSV from assets: ", e)
    }
    return userList
}

data class UserData(
    val phoneNumber: String,
    val userId: String,
    val sex: String,

    val HEIFAtotalscore: Double,

    val discretionaryHEIFAscore: Double,
    val vegetablesHEIFAscore: Double,
    val fruitHEIFAscore: Double,
    val grainsAndCerealsHEIFAscore: Double,
    val wholegrainsHEIFAscore: Double,
    val meatAndAlternativesHEIFAscore: Double,
    val dairyAndAlternativesHEIFAscore: Double,
    val sodiumHEIFAscore: Double,
    val alcoholHEIFAscore: Double,
    val waterHEIFAscore: Double,
    val sugarHEIFAscore: Double,
    val SaturatedFatHEIFAscore: Double,
    val unsaturatedFatHEIFAscore: Double,

    val fruitServeSize: Double,
    val fruitVariationScore: Double,
)