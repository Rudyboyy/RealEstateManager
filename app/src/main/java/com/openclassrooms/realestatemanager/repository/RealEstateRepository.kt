package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.dto.GeocodingApiResponse
import com.openclassrooms.realestatemanager.service.GeocodingApiService
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.Constants.MAPS_API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

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
            val location = geocodingApiResponse?.results?.firstOrNull()?.geometry?.location
            Pair(location?.lat, location?.lng)
        } else {
            // handle error
            Pair(null, null)
        }
    }
}