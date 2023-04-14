package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.model.Property

class DummyRealEstateProvider {

    companion object {
        val samplePropertyList = mutableListOf(
            Property(11542, "house"),
            Property(22542, "flat"),
            Property(332542, "lounge"),
            Property(44542, "house"),
            Property(55542, "flat")
        )
    }
}