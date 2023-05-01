package com.openclassrooms.realestatemanager.model.dto

data class GeocodingApiResponse(
    val results: List<Result>,
    val status: String
)

data class Result(
    val geometry: Geometry,
    val formatted_address: String
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)
