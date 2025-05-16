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
            val qDao = db.questionnaireDao()
            // TODO: use qDao?
        }
    }
}

fun loadCSV(context: Context): List<UserData> {
    val userList = mutableListOf<UserData>()

    try {
        val inputStream = context.assets.open("participantData.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            val allLines = lines.toList()
            if (allLines.isNotEmpty()) {
                allLines.drop(1).forEach { line ->
                    val tokens = line.split(",")
                    userList.add(
                        // TODO: use if elses to categorize by gender
                        UserData(
                            phoneNumber = tokens[0].trim().removePrefix("\""),
                            userId = tokens[1].trim(),
                            sex = tokens[2].trim(),
                            HEIFAtotalscoreMale = tokens[3].toDoubleOrNull() ?: 0.0,
                            HEIFAtotalscoreFemale = tokens[4].toDoubleOrNull() ?: 0.0,
                            discretionaryHEIFAscoreMale = tokens[5].toDoubleOrNull() ?: 0.0,
                            discretionaryHEIFAscoreFemale = tokens[6].toDoubleOrNull() ?: 0.0,
                            vegetablesHEIFAscoreMale = tokens[8].toDoubleOrNull() ?: 0.0,
                            vegetablesHEIFAscoreFemale = tokens[9].toDoubleOrNull() ?: 0.0,
                            fruitHEIFAscoreMale = tokens[19].toDoubleOrNull() ?: 0.0,
                            fruitHEIFAscoreFemale = tokens[20].toDoubleOrNull() ?: 0.0,

                            fruitServeSize = tokens[21].toDoubleOrNull() ?: 0.0,
                            fruitVariationScore = tokens[22].toDoubleOrNull() ?: 0.0,

                            grainsAndCerealsHEIFAscoreMale = tokens[29].toDoubleOrNull() ?: 0.0,
                            grainsAndCerealsHEIFAscoreFemale = tokens[30].toDoubleOrNull() ?: 0.0,
                            wholegrainsHEIFAscoreMale = tokens[33].toDoubleOrNull() ?: 0.0,
                            wholegrainsHEIFAscoreFemale = tokens[34].toDoubleOrNull() ?: 0.0,
                            meatAndAlternativesHEIFAscoreMale = tokens[36].toDoubleOrNull() ?: 0.0,
                            meatAndAlternativesHEIFAscoreFemale = tokens[37].toDoubleOrNull() ?: 0.0,
                            dairyAndAlternativesHEIFAscoreMale = tokens[40].toDoubleOrNull() ?: 0.0,
                            dairyAndAlternativesHEIFAscoreFemale = tokens[41].toDoubleOrNull() ?: 0.0,
                            sodiumHEIFAscoreMale = tokens[43].toDoubleOrNull() ?: 0.0,
                            sodiumHEIFAscoreFemale = tokens[44].toDoubleOrNull() ?: 0.0,
                            alcoholHEIFAscoreMale = tokens[46].toDoubleOrNull() ?: 0.0,
                            alcoholHEIFAscoreFemale = tokens[47].toDoubleOrNull() ?: 0.0,
                            waterHEIFAscoreMale = tokens[49].toDoubleOrNull() ?: 0.0,
                            waterHEIFAscoreFemale = tokens[50].toDoubleOrNull() ?: 0.0,
                            sugarHEIFAscoreMale = tokens[54].toDoubleOrNull() ?: 0.0,
                            sugarHEIFAscoreFemale = tokens[55].toDoubleOrNull() ?: 0.0,
                            SaturatedFatHEIFAscoreMale = tokens[57].toDoubleOrNull() ?: 0.0,
                            SaturatedFatHEIFAscoreFemale = tokens[58].toDoubleOrNull() ?: 0.0,
                            unsaturatedFatHEIFAscoreMale = tokens[60].toDoubleOrNull() ?: 0.0,
                            unsaturatedFatHEIFAscoreFemale = tokens[61].toDoubleOrNull() ?: 0.0
                        )
                    )
                }
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
    val sex : String,
    val HEIFAtotalscoreMale: Double,
    val HEIFAtotalscoreFemale: Double,
    val discretionaryHEIFAscoreMale: Double,
    val discretionaryHEIFAscoreFemale: Double,
    val vegetablesHEIFAscoreMale: Double,
    val vegetablesHEIFAscoreFemale: Double,
    val fruitHEIFAscoreMale: Double,
    val fruitHEIFAscoreFemale: Double,

    val fruitServeSize: Double,
    val fruitVariationScore: Double,

    val grainsAndCerealsHEIFAscoreMale: Double,
    val grainsAndCerealsHEIFAscoreFemale: Double,
    val wholegrainsHEIFAscoreMale: Double,
    val wholegrainsHEIFAscoreFemale: Double,
    val meatAndAlternativesHEIFAscoreMale: Double,
    val meatAndAlternativesHEIFAscoreFemale: Double,
    val dairyAndAlternativesHEIFAscoreMale: Double,
    val dairyAndAlternativesHEIFAscoreFemale: Double,
    val sodiumHEIFAscoreMale: Double,
    val sodiumHEIFAscoreFemale: Double,
    val alcoholHEIFAscoreMale: Double,
    val alcoholHEIFAscoreFemale: Double,
    val waterHEIFAscoreMale: Double,
    val waterHEIFAscoreFemale: Double,
    val sugarHEIFAscoreMale: Double,
    val sugarHEIFAscoreFemale: Double,
    val SaturatedFatHEIFAscoreMale: Double,
    val SaturatedFatHEIFAscoreFemale: Double,
    val unsaturatedFatHEIFAscoreMale: Double,
    val unsaturatedFatHEIFAscoreFemale: Double,
)