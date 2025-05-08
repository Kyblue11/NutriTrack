package com.aaronlamkongyew33521808.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aaronlamkongyew33521808.myapplication.UserData

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    @ColumnInfo val phoneNumber: String,
    @ColumnInfo val sex: String,
    @ColumnInfo val HEIFAtotalscoreMale: Double,
    @ColumnInfo val HEIFAtotalscoreFemale: Double
) {
    companion object {
        fun fromUserData(u: UserData) = UserEntity(
            userId = u.userId,
            phoneNumber = u.phoneNumber,
            sex = u.sex,
            HEIFAtotalscoreMale = u.HEIFAtotalscoreMale,
            HEIFAtotalscoreFemale = u.HEIFAtotalscoreFemale
        )
    }
}