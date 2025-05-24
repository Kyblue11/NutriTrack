package com.aaronlamkongyew33521808.myapplication.data.api

import retrofit2.http.GET

interface FruityViceApi {
    @GET("api/fruit/all")
    suspend fun getAllFruits(): List<Fruit>

// Is getting all, then filtering to find a specific fruit not optimal?
// Should i have a singular get fruit function?
// But, what if a user spams get requests many times? Then one getAll request is more efficient!
// Since the total number of fruits is small, it is okay to get all fruits at once!

// Also, the `/all` call solves the problem of the space bar " " API problem for fruits like "horned melon"
}