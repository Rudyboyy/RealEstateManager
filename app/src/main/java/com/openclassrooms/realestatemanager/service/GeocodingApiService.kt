package com.openclassrooms.realestatemanager.service

import com.openclassrooms.realestatemanager.model.dto.GeocodingApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {

    @GET("geocode/json")
    suspend fun geocodeAddress(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Response<GeocodingApiResponse>
}