package com.aaronlamkongyew33521808.myapplication.data.api

import retrofit2.http.GET

interface FruityViceApi {
    @GET("api/fruit/all")
    suspend fun getAllFruits(): List<Fruit>

    // TODO: is getting all, then filtering bad? Should i have a singular get fruit function? But what if a user spams get requests many times? Then one getAll request is more efficient!?
}