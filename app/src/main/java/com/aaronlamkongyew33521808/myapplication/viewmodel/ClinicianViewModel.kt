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

    fun loadAverages() = viewModelScope.launch(Dispatchers.IO) {
        val users = dao.getAllUsers()
        _avgMale.value = users.filter { it.sex == "Male" }
            .map { it.HEIFAtotalscore }
            .average()
        _avgFemale.value = users.filter { it.sex == "Female" }
            .map { it.HEIFAtotalscore }
            .average()
    }

    fun findPatterns() = viewModelScope.launch(Dispatchers.IO) {
        _loading.value = true
        try {
            // build a prompt including your averages or sample data
            val prompt = buildString {
                append("I have a dataset of HEIFA scores. ")
                append("Average male: ${_avgMale.value}, female: ${_avgFemale.value}. ")
                append("Identify three interesting patterns in plain sentences.") // TODO: add more context
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
