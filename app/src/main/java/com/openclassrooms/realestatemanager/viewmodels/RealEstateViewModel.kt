package com.openclassrooms.realestatemanager.viewmodels

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class RealEstateViewModel(
    private val repository: RealEstateRepository,
    private val executor: Executor
) : ViewModel() {

    private val _selectedProperty = MutableLiveData<Property>()
    val selectedProperty: LiveData<Property>
        get() = _selectedProperty

    val propertiesLiveData: LiveData<List<Property>> = repository.properties.asLiveData()

    private val _latitude = MutableLiveData<Double?>()
    val latitude: LiveData<Double?>
        get() = _latitude

    private val _longitude = MutableLiveData<Double?>()
    val longitude: LiveData<Double?>
        get() = _longitude

    fun updateCoordinatesFromAddress(address: String) {
        viewModelScope.launch {
            val (latitude, longitude) = repository.getCoordinatesFromAddress(address)
            _latitude.value = latitude
            _longitude.value = longitude
        }
    }

    fun getCoordinatesText(): String {
        val latitude = latitude.value
        val longitude = longitude.value
        return if (latitude != null && longitude != null) {
            "Latitude: $latitude\nLongitude: $longitude"
        } else {
            "Coordinates not available"
        }
    }

    fun updateSelectedProperty(property: Property) {
        _selectedProperty.value = property
    }

    fun update(property: Property) {
        executor.execute { viewModelScope.launch { repository.update(property) } }
    }

    fun updateCoordinatesFromAddress(property: Property, address: String) {
        viewModelScope.launch {
            val (latitude, longitude) = repository.getCoordinatesFromAddress(address)
            val updatedProperty = property.copy(latitude = latitude, longitude = longitude)
            repository.update(updatedProperty)
        }
    }

    fun addProperty(property: Property) {
        executor.execute { viewModelScope.launch { repository.invoke(property) } }
    }

    private val _minPrice = MutableLiveData<Int>()
    val minPrice: LiveData<Int>
        get() = _minPrice

    private val _maxPrice = MutableLiveData<Int>()
    val maxPrice: LiveData<Int>
        get() = _maxPrice

    val filteredProperties: LiveData<List<Property>> = MediatorLiveData<List<Property>>().apply {
        var properties: List<Property>? = null
        var minPrice: Int? = null
        var maxPrice: Int? = null

        val updateFilteredList: () -> Unit = {
            val filteredList = properties?.filter { property ->
                property.price >= (minPrice ?: 0) && property.price <= (maxPrice ?: Int.MAX_VALUE)
            }
            value = filteredList
        }

        addSource(propertiesLiveData) { prop ->
            properties = prop
            updateFilteredList()
        }

        addSource(_minPrice) { min ->
            minPrice = min
            updateFilteredList()
        }

        addSource(_maxPrice) { max ->
            maxPrice = max
            updateFilteredList()
        }
    }

    fun setPriceRange(minPrice: Int, maxPrice: Int) {
        _minPrice.value = minPrice
        _maxPrice.value = maxPrice
    }
}