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
    val HEIFAtotalscore: Double,

    // Sub‐scores
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

    // TODO: check this
    @ColumnInfo val name: String?,
    @ColumnInfo val passwordHash: String?,

    val fruitServeSize: Double,
    val fruitVariationScore: Double,
) {
    companion object {
        /** Map from your CSV-backed UserData into the Room entity. */
        fun fromUserData(u: UserData) = UserEntity(
            userId = u.userId,
            phoneNumber = u.phoneNumber,
            sex = u.sex,
            HEIFAtotalscore = u.HEIFAtotalscore,

            vegetablesHEIFAscore = u.vegetablesHEIFAscore,
            fruitHEIFAscore = u.fruitHEIFAscore,
            grainsAndCerealsHEIFAscore = u.grainsAndCerealsHEIFAscore,
            wholegrainsHEIFAscore = u.wholegrainsHEIFAscore,
            meatAndAlternativesHEIFAscore = u.meatAndAlternativesHEIFAscore,
            dairyAndAlternativesHEIFAscore = u.dairyAndAlternativesHEIFAscore,
            waterHEIFAscore = u.waterHEIFAscore,
            SaturatedFatHEIFAscore = u.SaturatedFatHEIFAscore,
            unsaturatedFatHEIFAscore = u.unsaturatedFatHEIFAscore,
            sodiumHEIFAscore = u.sodiumHEIFAscore,
            sugarHEIFAscore = u.sugarHEIFAscore,
            alcoholHEIFAscore = u.alcoholHEIFAscore,
            discretionaryHEIFAscore = u.discretionaryHEIFAscore,

            name = null,
            passwordHash = null,

            fruitServeSize = u.fruitServeSize,
            fruitVariationScore = u.fruitVariationScore,
        )
    }
}
