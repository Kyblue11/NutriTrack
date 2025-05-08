package com.aaronlamkongyew33521808.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aaronlamkongyew33521808.myapplication.data.AppDatabase
import com.aaronlamkongyew33521808.myapplication.data.entity.QuestionnaireEntity
import com.aaronlamkongyew33521808.myapplication.repository.QuestionnaireRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionnaireViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = QuestionnaireRepository(
        AppDatabase.getDatabase(application).questionnaireDao()
    )

    private val _fruits = MutableStateFlow(false)
    val fruits: StateFlow<Boolean> = _fruits.asStateFlow()

    private val _vegetables = MutableStateFlow(false)
    val vegetables: StateFlow<Boolean> = _vegetables.asStateFlow()

    private val _grains = MutableStateFlow(false)
    val grains: StateFlow<Boolean> = _grains.asStateFlow()

    private val _redMeat = MutableStateFlow(false)
    val redMeat: StateFlow<Boolean> = _redMeat.asStateFlow()

    private val _seafood = MutableStateFlow(false)
    val seafood: StateFlow<Boolean> = _seafood.asStateFlow()

    private val _poultry = MutableStateFlow(false)
    val poultry: StateFlow<Boolean> = _poultry.asStateFlow()

    private val _fish = MutableStateFlow(false)
    val fish: StateFlow<Boolean> = _fish.asStateFlow()

    private val _eggs = MutableStateFlow(false)
    val eggs: StateFlow<Boolean> = _eggs.asStateFlow()

    private val _nutsSeeds = MutableStateFlow(false)
    val nutsSeeds: StateFlow<Boolean> = _nutsSeeds.asStateFlow()

    private val _persona = MutableStateFlow("")
    val persona: StateFlow<String> = _persona.asStateFlow()

    private val _biggestMealTime = MutableStateFlow("")
    val biggestMealTime: StateFlow<String> = _biggestMealTime.asStateFlow()

    private val _sleepTime = MutableStateFlow("")
    val sleepTime: StateFlow<String> = _sleepTime.asStateFlow()

    private val _wakeTime = MutableStateFlow("")
    val wakeTime: StateFlow<String> = _wakeTime.asStateFlow()

    fun load(userId: String) = viewModelScope.launch {
        repo.load(userId)?.let { q ->
            _fruits.value = q.fruits
            _vegetables.value = q.vegetables
            _grains.value = q.grains
            _redMeat.value = q.redMeat
            _seafood.value = q.seafood
            _poultry.value = q.poultry
            _fish.value = q.fish
            _eggs.value = q.eggs
            _nutsSeeds.value = q.nutsSeeds
            _persona.value = q.persona
            _biggestMealTime.value = q.biggestMealTime
            _sleepTime.value = q.sleepTime
            _wakeTime.value = q.wakeTime
        }
    }

    fun setFruits(v: Boolean) {
        _fruits.value = v
    }
    fun setVegetables(v: Boolean) {
        _vegetables.value = v
    }
    fun setGrains(v: Boolean) {
        _grains.value = v
    }
    fun setRedMeat(v: Boolean) {
        _redMeat.value = v
    }
    fun setSeafood(v: Boolean) {
        _seafood.value = v
    }
    fun setPoultry(v: Boolean) {
        _poultry.value = v
    }
    fun setFish(v: Boolean) {
        _fish.value = v
    }
    fun setEggs(v: Boolean) {
        _eggs.value = v
    }
    fun setNutsSeeds(v: Boolean) {
        _nutsSeeds.value = v
    }
    fun setPersona(v: String) {
        _persona.value = v
    }
    fun setBiggestMealTime(v: String) {
        _biggestMealTime.value = v
    }
    fun setSleepTime(v: String) {
        _sleepTime.value = v
    }
    fun setWakeTime(v: String) {
        _wakeTime.value = v
    }

    fun save(userId: String, onComplete: () -> Unit) = viewModelScope.launch {
        val entity = QuestionnaireEntity(
            userId = userId,
            fruits = fruits.value,
            vegetables = vegetables.value,
            grains = grains.value,
            redMeat = redMeat.value,
            seafood = seafood.value,
            poultry = poultry.value,
            fish = fish.value,
            eggs = eggs.value,
            nutsSeeds = nutsSeeds.value,
            persona = persona.value,
            biggestMealTime = biggestMealTime.value,
            sleepTime = sleepTime.value,
            wakeTime = wakeTime.value
        )
        repo.save(entity)
        onComplete()
    }
}
