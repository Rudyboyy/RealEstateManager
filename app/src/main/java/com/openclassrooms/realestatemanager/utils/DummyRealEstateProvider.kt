package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.model.RealEstate

class DummyRealEstateProvider {

    companion object {
        val samplePropertyList = mutableListOf(
            RealEstate(11542, "house"),
            RealEstate(22542, "flat"),
            RealEstate(332542, "lounge"),
            RealEstate(44542, "house"),
            RealEstate(55542, "flat")
        )
    }
}