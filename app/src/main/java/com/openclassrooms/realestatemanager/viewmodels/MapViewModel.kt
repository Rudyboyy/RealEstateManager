package com.openclassrooms.realestatemanager.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val _userLocation = MutableLiveData<LatLng>()

    val userLocation: LiveData<LatLng>
        get() = _userLocation

    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
    }
}