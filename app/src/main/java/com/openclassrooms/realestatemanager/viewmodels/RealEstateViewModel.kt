package com.openclassrooms.realestatemanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import java.util.concurrent.Executor

class RealEstateViewModel(
    private val repository: RealEstateRepository,
    private val executor: Executor
) : ViewModel() {

    val allProperties: LiveData<List<Property>> = repository.allProperties.asLiveData()

    suspend fun update(property: Property): Int {
        return repository.update(property)
    }
}