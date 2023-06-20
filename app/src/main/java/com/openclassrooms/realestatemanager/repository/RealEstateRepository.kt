package com.openclassrooms.realestatemanager.repository

import android.util.Log
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.service.GeocodingApiService
import com.openclassrooms.realestatemanager.utils.Constants.MAPS_API_KEY
import kotlinx.coroutines.flow.Flow

class RealEstateRepository(
    private val propertyDao: PropertyDao,
    private val geocodingApiService: GeocodingApiService) {

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
}