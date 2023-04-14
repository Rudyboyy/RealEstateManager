package com.openclassrooms.realestatemanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.model.RealEstate
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import java.util.concurrent.Executor

class RealEstateViewModel(
    private val repository: RealEstateRepository,
    private val executor: Executor
) : ViewModel() {

    val allProperties: LiveData<List<RealEstate>> = repository.allProperties.asLiveData()

    suspend fun update(realEstate: RealEstate): Int {
        return repository.update(realEstate)
    }
}