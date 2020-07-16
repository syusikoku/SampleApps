package com.zy.jet.notebook.jetpack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zy.jet.notebook.jetpack.repository.PlantDbRepository
import com.zy.jet.notebook.jetpack.room.entry.Plant

/**
 * PlantViewModel
 */
class PlantViewModel(private val repository: PlantDbRepository) : ViewModel() {
    private val _list = repository.getPlantList()
    val dataList: LiveData<List<Plant>> = _list
}