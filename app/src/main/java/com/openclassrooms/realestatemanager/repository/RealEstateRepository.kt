package com.openclassrooms.realestatemanager.repository

import android.util.Log
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.service.GeocodingApiService
import com.openclassrooms.realestatemanager.utils.Constants.MAPS_API_KEY
import com.openclassrooms.realestatemanager.utils.Constants.SORT_BY_DATE_ASCENDING
import com.openclassrooms.realestatemanager.utils.Constants.SORT_BY_DATE_DESCENDING
import com.openclassrooms.realestatemanager.utils.Constants.SORT_BY_PRICE_ASCENDING
import com.openclassrooms.realestatemanager.utils.Constants.SORT_BY_PRICE_DESCENDING
import kotlinx.coroutines.flow.Flow

class RealEstateRepository(
    private val propertyDao: PropertyDao,
    private val geocodingApiService: GeocodingApiService
) {

    val properties: Flow<List<Property>> = propertyDao.getAllProperties()

    suspend fun invoke(propertyEntity: Property) {
        propertyDao.insert(propertyEntity)
    }

    suspend fun update(property: Property) {
        propertyDao.update(property)
    }

    suspend fun getCoordinatesFromAddress(address: String): Pair<Double?, Double?> {
        val response = geocodingApiService.geocodeAddress(address, MAPS_API_KEY)
        return if (response.isSuccessful) {
            val geocodingApiResponse = response.body()
            Log.d("API Response", "Geocoding API Response: $geocodingApiResponse")
            val location = geocodingApiResponse?.results?.firstOrNull()?.geometry?.location
            Log.d("API Response", "Location: $location")
            Pair(location?.lat, location?.lng)
        } else {
            // handle error
            Pair(null, null)
        }
    }

    fun getFilteredProperties(
        minPrice: Int?,
        maxPrice: Int?,
        minSurface: Int?,
        maxSurface: Int?,
        minPhoto: Int?,
        poiList: String,
        sortingOption: List<String>?
    ): List<Property> {
        val filteredProperties = propertyDao.getFilteredProperties(
            minPrice,
            maxPrice,
            minSurface,
            maxSurface,
            poiList
        )

        val propertiesWithMinPhotos = filteredProperties.filter { property ->
            property.photos.size >= (minPhoto ?: 0)
        }

        val sortedList = when {
            sortingOption != null && sortingOption.isNotEmpty() -> {
                when (sortingOption.first()) {
                    SORT_BY_PRICE_ASCENDING -> propertiesWithMinPhotos.sortedBy { it.price }
                    SORT_BY_PRICE_DESCENDING -> propertiesWithMinPhotos.sortedByDescending { it.price }
                    SORT_BY_DATE_ASCENDING -> propertiesWithMinPhotos.sortedBy { it.entryDate }
                    SORT_BY_DATE_DESCENDING -> propertiesWithMinPhotos.sortedByDescending { it.entryDate }
                    else -> propertiesWithMinPhotos
                }
            }
            else -> propertiesWithMinPhotos
        }

        return sortedList
    }

}