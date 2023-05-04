package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.BuildConfig

object Constants {

    const val BASE_URL_STATIC_MAP = "https://maps.googleapis.com/maps/api/staticmap?"
    const val DEFAULT_ZOOM_AND_SIZE = "zoom=15&size=400x400"
    const val DEFAULT_MARKER_TYPE = "markers=color:red|label:%7C|"
    const val BASE_URL = "https://maps.googleapis.com/maps/api/"
    const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY
    const val REQUEST_CODE_UPDATE_LOCATION = 541
    const val DIALOG = "dialog"
}