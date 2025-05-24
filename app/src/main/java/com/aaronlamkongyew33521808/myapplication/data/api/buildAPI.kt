package com.aaronlamkongyew33521808.myapplication.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object buildAPI {
    private const val BASE_URL = "https://www.fruityvice.com/"

    // 1) single Retrofit instance
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 2) and API interface
    val fruityApi: FruityViceApi by lazy {
        retrofit.create(FruityViceApi::class.java)
    }
}