package com.aaronlamkongyew33521808.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aaronlamkongyew33521808.myapplication.UserData

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val phoneNumber: String,
    val sex: String,

    // Total HEIFA scores
    val HEIFAtotalscoreMale: Double,
    val HEIFAtotalscoreFemale: Double,

    // Sub‐scores (male/female)
    val vegetablesHEIFAscoreMale: Double,
    val vegetablesHEIFAscoreFemale: Double,
    val fruitHEIFAscoreMale: Double,
    val fruitHEIFAscoreFemale: Double,
    val grainsAndCerealsHEIFAscoreMale: Double,
    val grainsAndCerealsHEIFAscoreFemale: Double,
    val wholegrainsHEIFAscoreMale: Double,
    val wholegrainsHEIFAscoreFemale: Double,
    val meatAndAlternativesHEIFAscoreMale: Double,
    val meatAndAlternativesHEIFAscoreFemale: Double,
    val dairyAndAlternativesHEIFAscoreMale: Double,
    val dairyAndAlternativesHEIFAscoreFemale: Double,
    val waterHEIFAscoreMale: Double,
    val waterHEIFAscoreFemale: Double,
    val SaturatedFatHEIFAscoreMale: Double,
    val SaturatedFatHEIFAscoreFemale: Double,
    val unsaturatedFatHEIFAscoreMale: Double,
    val unsaturatedFatHEIFAscoreFemale: Double,
    val sodiumHEIFAscoreMale: Double,
    val sodiumHEIFAscoreFemale: Double,
    val sugarHEIFAscoreMale: Double,
    val sugarHEIFAscoreFemale: Double,
    val alcoholHEIFAscoreMale: Double,
    val alcoholHEIFAscoreFemale: Double,
    val discretionaryHEIFAscoreMale: Double,
    val discretionaryHEIFAscoreFemale: Double,

    // TODO: check this
    @ColumnInfo val name: String?,
    @ColumnInfo val passwordHash: String?
) {
    companion object {
        /** Map from your CSV-backed UserData into the Room entity. */
        fun fromUserData(u: UserData) = UserEntity(
            userId = u.userId,
            phoneNumber = u.phoneNumber,
            sex = u.sex,
            HEIFAtotalscoreMale = u.HEIFAtotalscoreMale,
            HEIFAtotalscoreFemale = u.HEIFAtotalscoreFemale,
            vegetablesHEIFAscoreMale = u.vegetablesHEIFAscoreMale,
            vegetablesHEIFAscoreFemale = u.vegetablesHEIFAscoreFemale,
            fruitHEIFAscoreMale = u.fruitHEIFAscoreMale,
            fruitHEIFAscoreFemale = u.fruitHEIFAscoreFemale,
            grainsAndCerealsHEIFAscoreMale = u.grainsAndCerealsHEIFAscoreMale,
            grainsAndCerealsHEIFAscoreFemale = u.grainsAndCerealsHEIFAscoreFemale,
            wholegrainsHEIFAscoreMale = u.wholegrainsHEIFAscoreMale,
            wholegrainsHEIFAscoreFemale = u.wholegrainsHEIFAscoreFemale,
            meatAndAlternativesHEIFAscoreMale = u.meatAndAlternativesHEIFAscoreMale,
            meatAndAlternativesHEIFAscoreFemale = u.meatAndAlternativesHEIFAscoreFemale,
            dairyAndAlternativesHEIFAscoreMale = u.dairyAndAlternativesHEIFAscoreMale,
            dairyAndAlternativesHEIFAscoreFemale = u.dairyAndAlternativesHEIFAscoreFemale,
            waterHEIFAscoreMale = u.waterHEIFAscoreMale,
            waterHEIFAscoreFemale = u.waterHEIFAscoreFemale,
            SaturatedFatHEIFAscoreMale = u.SaturatedFatHEIFAscoreMale,
            SaturatedFatHEIFAscoreFemale = u.SaturatedFatHEIFAscoreFemale,
            unsaturatedFatHEIFAscoreMale = u.unsaturatedFatHEIFAscoreMale,
            unsaturatedFatHEIFAscoreFemale = u.unsaturatedFatHEIFAscoreFemale,
            sodiumHEIFAscoreMale = u.sodiumHEIFAscoreMale,
            sodiumHEIFAscoreFemale = u.sodiumHEIFAscoreFemale,
            sugarHEIFAscoreMale = u.sugarHEIFAscoreMale,
            sugarHEIFAscoreFemale = u.sugarHEIFAscoreFemale,
            alcoholHEIFAscoreMale = u.alcoholHEIFAscoreMale,
            alcoholHEIFAscoreFemale = u.alcoholHEIFAscoreFemale,
            discretionaryHEIFAscoreMale = u.discretionaryHEIFAscoreMale,
            discretionaryHEIFAscoreFemale = u.discretionaryHEIFAscoreFemale,

            name = null,
            passwordHash = null
        )
    }
}
