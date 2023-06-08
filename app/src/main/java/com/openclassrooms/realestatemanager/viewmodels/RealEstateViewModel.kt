package com.openclassrooms.realestatemanager.viewmodels

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.utils.Constants.SORT_BY_DATE_ASCENDING
import com.openclassrooms.realestatemanager.utils.Constants.SORT_BY_DATE_DESCENDING
import com.openclassrooms.realestatemanager.utils.Constants.SORT_BY_PRICE_ASCENDING
import com.openclassrooms.realestatemanager.utils.Constants.SORT_BY_PRICE_DESCENDING
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class RealEstateViewModel(
    private val repository: RealEstateRepository,
    private val executor: Executor
) : ViewModel() {

    private val _selectedProperty = MutableLiveData<Property>()
    val selectedProperty: LiveData<Property>
        get() = _selectedProperty

    private val propertiesLiveData: LiveData<List<Property>> = repository.properties.asLiveData()

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

    fun addProperty(property: Property, callback: (Boolean) -> Unit) {
        executor.execute {
            viewModelScope.launch {
                try {
                    repository.invoke(property)
                    callback(true)
                } catch (e: Exception) {
                    callback(false)
                }
            }
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

    val filteredProperties: LiveData<List<Property>> = MediatorLiveData<List<Property>>().apply {
        var properties: List<Property>? = null
        var minPrice: Int? = null
        var maxPrice: Int? = null
        var minSurface: Int? = null
        var maxSurface: Int? = null
        var minPhoto: Int? = null
        var poiList: List<String>? = null

        val updateFilteredList: () -> Unit = {
            val filteredList = properties?.filter { property ->
                val propertyPoints = property.pointOfInterest.split(", ")
                val requiredPoints = poiList ?: emptyList()

                requiredPoints.all { requiredPoint ->
                    propertyPoints.any { propertyPoint ->
                        propertyPoint == requiredPoint
                    }
                }
                        && property.price >= (minPrice ?: 0)
                        && property.price <= (maxPrice ?: Int.MAX_VALUE)
                        && property.surface >= (minSurface ?: 0)
                        && property.surface <= (maxSurface ?: Int.MAX_VALUE)
                        && property.photos.size >= (minPhoto ?: 0)
            }
            val sortedList = filteredList?.toMutableList()

            for (option in _sortingOption.value.orEmpty()) {
                when (option) {
                    SORT_BY_PRICE_ASCENDING -> sortedList?.sortBy { it.price }
                    SORT_BY_PRICE_DESCENDING -> sortedList?.sortByDescending { it.price }
                    SORT_BY_DATE_ASCENDING -> sortedList?.sortBy { it.entryDate }
                    SORT_BY_DATE_DESCENDING -> sortedList?.sortByDescending { it.entryDate }
                }
            }
            value = sortedList
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

        addSource(_minSurface) { max ->
            minSurface = max
            updateFilteredList()
        }

        addSource(_maxSurface) { max ->
            maxSurface = max
            updateFilteredList()
        }

        addSource(_minPhoto) { min ->
            minPhoto = min
            updateFilteredList()
        }

        addSource(_poiList) { poi ->
            poiList = poi
            updateFilteredList()
        }

        addSource(_sortingOption) {
            updateFilteredList()
        }
    }

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
}