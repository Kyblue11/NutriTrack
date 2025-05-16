package com.aaronlamkongyew33521808.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aaronlamkongyew33521808.myapplication.BuildConfig
import com.aaronlamkongyew33521808.myapplication.data.dao.UserDao
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClinicianViewModel(private val dao: UserDao) : ViewModel() {
    private val _avgMale   = MutableStateFlow(0.0)
    val avgMale: StateFlow<Double> = _avgMale

    private val _avgFemale = MutableStateFlow(0.0)
    val avgFemale: StateFlow<Double> = _avgFemale

    private val _insights  = MutableStateFlow<List<String>>(emptyList())
    val insights: StateFlow<List<String>> = _insights

    private val _loading   = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _avgMaleSatFats = MutableStateFlow(0.0)
    val avgMaleSatFats: StateFlow<Double> = _avgMaleSatFats

    private val _avgFemaleSatFats = MutableStateFlow(0.0)
    val avgFemaleSatFats: StateFlow<Double> = _avgFemaleSatFats

    private val _avgMaleUnsatFats = MutableStateFlow(0.0)
    val avgMaleUnsatFats: StateFlow<Double> = _avgMaleUnsatFats

    private val _avgFemaleUnsatFats = MutableStateFlow(0.0)
    val avgFemaleUnsatFats: StateFlow<Double> = _avgFemaleUnsatFats

    private val _totalFatsMale = MutableStateFlow(0.0)
    val totalFatsMale: StateFlow<Double> = _totalFatsMale

    private val _totalFatsFemale = MutableStateFlow(0.0)
    val totalFatsFemale: StateFlow<Double> = _totalFatsFemale

    private val _avgMaleFruits = MutableStateFlow(0.0)
    val avgMaleFruits: StateFlow<Double> = _avgMaleFruits

    private val _avgFemaleFruits = MutableStateFlow(0.0)
    val avgFemaleFruits: StateFlow<Double> = _avgFemaleFruits

    private val _avgMaleVegetables = MutableStateFlow(0.0)
    val avgMaleVegetables: StateFlow<Double> = _avgMaleVegetables

    private val _avgFemaleVegetables = MutableStateFlow(0.0)
    val avgFemaleVegetables: StateFlow<Double> = _avgFemaleVegetables

    private val _totalHealthyFoodsMale = MutableStateFlow(0.0)
    val totalHealthyFoodsMale: StateFlow<Double> = _totalHealthyFoodsMale

    private val _totalHealthyFoodsFemale = MutableStateFlow(0.0)
    val totalHealthyFoodsFemale: StateFlow<Double> = _totalHealthyFoodsFemale

    fun loadAverages() = viewModelScope.launch(Dispatchers.IO) {
        val users = dao.getAllUsers()
        _avgMale.value = users.filter { it.sex == "Male" }
            .map { it.HEIFAtotalscore }
            .average()
        _avgFemale.value = users.filter { it.sex == "Female" }
            .map { it.HEIFAtotalscore }
            .average()
    }

    fun loadAveragesForFats() = viewModelScope.launch(Dispatchers.IO) {
        val users = dao.getAllUsers()
        _avgMaleUnsatFats.value = users.filter { it.sex == "Male" &&  it.unsaturatedFatHEIFAscore != 0.0 }
            .map { it.unsaturatedFatHEIFAscore }
            .average()
        _avgFemaleUnsatFats.value = users.filter { it.sex == "Female" && it.unsaturatedFatHEIFAscore != 0.0 }
            .map { it.unsaturatedFatHEIFAscore }
            .average()

        _avgMaleSatFats.value = users.filter { it.sex == "Male" && it.SaturatedFatHEIFAscore != 0.0 }
            .map { it.SaturatedFatHEIFAscore }
            .average()
        _avgFemaleSatFats.value = users.filter { it.sex == "Female" && it.SaturatedFatHEIFAscore != 0.0 }
            .map { it.SaturatedFatHEIFAscore }
            .average()

        _totalFatsMale.value = (_avgMaleSatFats.value + _avgMaleUnsatFats.value ) / 2
        _totalFatsFemale.value = (_avgFemaleSatFats.value + _avgFemaleUnsatFats.value ) / 2
    }

    fun loadAveragesForHealthyFoods() = viewModelScope.launch(Dispatchers.IO) {
        val users = dao.getAllUsers()
        _avgMaleFruits.value = users.filter { it.sex == "Male" && it.fruitHEIFAscore != 0.0 }
            .map { it.fruitHEIFAscore }
            .average()
        _avgFemaleFruits.value = users.filter { it.sex == "Female" && it.fruitHEIFAscore != 0.0 }
            .map { it.fruitHEIFAscore }
            .average()

        _avgMaleVegetables.value = users.filter { it.sex == "Male" && it.vegetablesHEIFAscore != 0.0 }
            .map { it.vegetablesHEIFAscore }
            .average()
        _avgFemaleVegetables.value = users.filter { it.sex == "Female" && it.vegetablesHEIFAscore != 0.0 }
            .map { it.vegetablesHEIFAscore }
            .average()

        _totalHealthyFoodsMale.value = (_avgMaleFruits.value + _avgMaleVegetables.value) / 2
        _totalHealthyFoodsFemale.value = (_avgFemaleFruits.value + _avgFemaleVegetables.value) / 2
    }

    fun findPatterns() = viewModelScope.launch(Dispatchers.IO) {
        _loading.value = true
        try {
            // build a prompt including your averages or sample data
            val prompt = buildString {
                append("I have a dataset of HEIFA scores for both male and female users. ")
                append("Average total HEIFA scores for male: ${_avgMale.value}, female: ${_avgFemale.value}. ")
                append("Average score for fats (both saturated and unsaturated included) was ${_totalFatsMale.value} for male and ${_totalFatsFemale.value} for females. ")
                append("The average score for saturated fats was ${_avgMaleSatFats.value} for male and ${_avgFemaleSatFats.value} for females. ")
                append("The average score for unsaturated fats was ${_avgMaleUnsatFats.value} for male and ${_avgFemaleUnsatFats.value} for females. ")
                append("Average score for healthy foods (both fruits and vegetables included) was ${_totalHealthyFoodsMale.value} for makes and ${_totalHealthyFoodsFemale.value} for females")
                append("Identify three interesting patterns in plain sentences.")
                append("Make sure to include the numerical scores in your response.")
                append("By inferring to the healthy food scores and fat scores, the third pattern should be a bold assumption regarding males vs females")
                append("Do not output any formalities (e.g. Here are the patterns I found), just the actual 3 sentences themselves, separated by new lines.")
            }

            // call your GenerativeModel (e.g. Gemini client)
            val model = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey    = BuildConfig.apiKey
            )
            val resp = model.generateContent(prompt)
            // split into three lines (or however your model returns)
            val lines = resp.text?.split("\n")?.filter { it.isNotBlank() } ?: listOf("No insight.")
            _insights.value = lines.take(3)
        } catch (e: Exception) {
            _insights.value = listOf("Error fetching insights: ${e.localizedMessage}")
        } finally {
            _loading.value = false
        }
    }

    class Factory(private val dao: UserDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClinicianViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ClinicianViewModel(dao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}
