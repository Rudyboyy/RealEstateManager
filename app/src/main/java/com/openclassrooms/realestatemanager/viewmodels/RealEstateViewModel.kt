package com.openclassrooms.realestatemanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import java.util.concurrent.Executor

class RealEstateViewModel(
    private val repository: RealEstateRepository,
    private val executor: Executor
) : ViewModel() {

    private val _selectedProperty = MutableLiveData<Property>()
    val selectedProperty: LiveData<Property>
        get() = _selectedProperty

    val propertiesLiveData: LiveData<List<Property>> = repository.properties.asLiveData()

//    init {
//        viewModelScope.launch {
//            val properties = withContext(Dispatchers.IO) {
//                repository.properties.first()
//            }
//            _selectedProperty.postValue(properties.first())
//            _selectedProperty.postValue(propertiesLiveData.value?.get(0))
//        }
//    }

    fun updateSelectedProperty(property: Property) {
        _selectedProperty.value = property
    }

    suspend fun update(property: Property) {
        return repository.update(property)
    }
}