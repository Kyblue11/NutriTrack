package com.aaronlamkongyew33521808.myapplication.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.data.api.Fruit
import com.aaronlamkongyew33521808.myapplication.data.api.FruityViceApi
import com.aaronlamkongyew33521808.myapplication.data.entity.NutriCoachTip
import com.aaronlamkongyew33521808.myapplication.repository.NutriCoachRepository
import dev.shreyaspatil.ai.client.generativeai.BuildConfig
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NutriCoachViewModel(
    private val repo: NutriCoachRepository,
    private val context: Context
) : ViewModel() {
    private val _fruits = MutableStateFlow<List<Fruit>>(emptyList())
    val fruits: StateFlow<List<Fruit>> = _fruits

    private val _tips = MutableStateFlow<List<NutriCoachTip>>(emptyList())
    val tips: StateFlow<List<NutriCoachTip>> = _tips

    private val _genTip = MutableStateFlow<String?>(null)
    val genTip: StateFlow<String?> = _genTip

    suspend fun fetchFruits() { // TODO: is this bad practice?
        try {
            _fruits.value = repo.fetchFruits()
        } catch (e: Exception) {
            _fruits.value = emptyList() // Handle error gracefully
            e.printStackTrace() // Log the error for debugging
        }
    }

    fun generateTip(userId: String, prompt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val model = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = "AIzaSyDD-irqROmXNTzOwcvrH4P-QcQald2prhE"
                )
                val response = model.generateContent(prompt)
                val tip = response.text ?: return@launch
                val tipEntity = NutriCoachTip(userId = userId, tip = tip)
                repo.saveTip(tipEntity)
                _genTip.value = tip
                _tips.value = repo.getTips(userId)
            } catch (e: Exception) {
                    Log.e("NutriCoachVM", "AI generation failed", e)
                    _genTip.value = "Failed to fetch tip: ${e.localizedMessage}"
                }
        }
    }

    fun loadTipHistory(userId: String) {
        viewModelScope.launch {
            _tips.value = repo.getTips(userId)
        }
    }
}

//class NutriCoachViewModel(application: Application)
//    : AndroidViewModel(application) {
//
//    // get your DAOs / Retrofit from a singleton or AppDatabase
//    private val db   = AppDatabase.getDatabase(application)
//    private val dao  = db.nutriCoachDao()
//    private val api  = Retrofit.Builder()
//        .baseUrl("https://fruityvice.com/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(FruityViceApi::class.java)
//
//
//
//    private val repo = NutriCoachRepository(api, dao)
//    private val _fruits = MutableStateFlow<List<Fruit>>(emptyList())
//    val fruits: StateFlow<List<Fruit>> = _fruits
//
//    private val _tips = MutableStateFlow<List<NutriCoachTip>>(emptyList())
//    val tips: StateFlow<List<NutriCoachTip>> = _tips
//
//    private val _genTip = MutableStateFlow<String?>(null)
//    val genTip: StateFlow<String?> = _genTip
//
//    suspend fun fetchFruits() {
//        _fruits.value = repo.fetchFruits()
//    }
//
//    fun generateTip(userId: String, prompt: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val model = GenerativeModel(
//                    modelName = "gemini-1.5-flash",
//                    apiKey = "AIzaSyDD-irqROmXNTzOwcvrH4P-QcQald2prhE"
//                )
//                val response = model.generateContent(prompt)
//                val tip = response.text ?: return@launch
//                val tipEntity = NutriCoachTip(userId = userId, tip = tip)
//                repo.saveTip(tipEntity)
//                _genTip.value = tip
//                _tips.value = repo.getTips(userId)
//            } catch (e: Exception) {
//                _genTip.value = "Error: ${e.localizedMessage}"
//            }
//        }
//    }
//
//    fun loadTipHistory(userId: String) {
//        viewModelScope.launch {
//            _tips.value = repo.getTips(userId)
//        }
//    }
//}