package com.openclassrooms.realestatemanager.viewmodels

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor

class RealEstateViewModel(
    private val repository: RealEstateRepository,
    private val executor: Executor
) : ViewModel() {

    private val _selectedProperty = MutableLiveData<Property>()
    val selectedProperty: LiveData<Property>
        get() = _selectedProperty

    val propertiesLiveData: LiveData<List<Property>> = repository.properties.asLiveData()

    fun updateSelectedProperty(property: Property) {
        _selectedProperty.value = property
    }

    fun update(property: Property) {
        executor.execute {
            viewModelScope.launch {
                val updatedProperty = updateCoordinatesFromAddress(property)
                repository.update(updatedProperty)
            }
        }
    }

    private suspend fun updateCoordinatesFromAddress(property: Property): Property {
        return withContext(Dispatchers.IO) {
            val (latitude, longitude) = repository.getCoordinatesFromAddress(
                "${property.address} ${property.postalCode} ${property.country}"
            )
            property.copy(latitude = latitude, longitude = longitude)
        }
    }

    fun addProperty(property: Property, callback: (Boolean) -> Unit) {
        executor.execute {
            viewModelScope.launch {
                try {
                    val updatedProperty = updateCoordinatesFromAddress(property)
                    repository.invoke(updatedProperty)
                    callback(true)
                } catch (e: Exception) {
                    callback(false)
                }
            }
        }
    }

    fun applyFilters(
        minPrice: Int?,
        maxPrice: Int?,
        minSurface: Int?,
        maxSurface: Int?,
        minPhoto: Int?,
        poiList: String,
        sortingOption: List<String>?
    ) {
        viewModelScope.launch {
            val properties = withContext(Dispatchers.IO) {
                repository.getFilteredProperties(
                    minPrice,
                    maxPrice,
                    minSurface,
                    maxSurface,
                    minPhoto,
                    poiList,
                    sortingOption
                )
            }
            (propertiesLiveData as MutableLiveData<List<Property>>).value = properties
        }
    }

    private val _minPrice = MutableLiveData<Int>()
    val minPrice: LiveData<Int>
        get() = _minPrice

    private val _maxPrice = MutableLiveData<Int>()
    val maxPrice: LiveData<Int>
        get() = _maxPrice

    private val _minSurface = MutableLiveData<Int>()
    val minSurface: LiveData<Int>
        get() = _minSurface

    private val _maxSurface = MutableLiveData<Int>()
    val maxSurface: LiveData<Int>
        get() = _maxSurface

    private val _minPhoto = MutableLiveData<Int>()
    val minPhoto: LiveData<Int>
        get() = _minPhoto

    private val _poiList = MutableLiveData<List<String>>()
    val poiList: LiveData<List<String>>
        get() = _poiList

    private val _sortingOption = MutableLiveData<List<String>>()
    val sortingOption: LiveData<List<String>>
        get() = _sortingOption

    fun setPriceRange(minPrice: Int, maxPrice: Int) {
        _minPrice.value = minPrice
        _maxPrice.value = maxPrice
    }

    fun setSurfaceRange(minSurface: Int, maxSurface: Int) {
        _minSurface.value = minSurface
        _maxSurface.value = maxSurface
    }

    fun setMinPhoto(minPhoto: Int) {
        _minPhoto.value = minPhoto
    }

    fun setPoiList(poiList: List<String>) {
        _poiList.value = poiList
    }

    fun setSortingOption(option: List<String>) {
        _sortingOption.value = option
    }

    val currencyLiveData = MutableLiveData<Boolean>()

    fun setCurrency(isDollar: Boolean) {
        currencyLiveData.value = isDollar
    }
}