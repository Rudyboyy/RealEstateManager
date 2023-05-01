package com.openclassrooms.realestatemanager.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.service.GeocodingApiService
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import java.util.concurrent.Executor

class ViewModelFactory(
    private val realEstateRepository: RealEstateRepository,
    private val executor: Executor
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RealEstateViewModel::class.java)) {
            return RealEstateViewModel(realEstateRepository, executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}