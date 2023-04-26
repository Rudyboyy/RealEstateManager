package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.PropertyStatus
import java.util.*

class DummyRealEstateProvider {

    companion object {

        private val entryDate: Calendar = Calendar.getInstance().apply {
            set(2018, Calendar.JANUARY, 3)
        }

        private val saleDate: Calendar = Calendar.getInstance().apply {
            set(2021, Calendar.DECEMBER, 25)
        }

        val samplePropertyList = mutableListOf(
            Property(
                id = 1,
                agent = "Bob Smith",
                type = "House",
                price = 350000.0,
                description = "Superbe maison de 6 pièces dans un quartier calme",//todo add constant in utils and get latin speech
                address = "123 Main St, Anytown",
                status = PropertyStatus.AVAILABLE,
                numberOfRooms = 6,
                numberOfBedrooms = 3,
                numberOfBathrooms = 1,
                surface = 150.0,
                pointOfInterest = "school, grocery store, park",
                entryDate = Date(System.currentTimeMillis()),
                latitude = 48.8566,
                longitude = 2.3522,
                photos = listOf(
                    Photo(
                        description = "Facade",
                        uri = "content://com.android.providers.media.documents/document/image%3A1234"
                    ),
                    Photo(
                        description = "Kitchen",
                        uri = "content://com.android.providers.media.documents/document/image%3A5678"
                    )
                )
            ),
            Property(
                id = 2,
                agent = "Jane Doe",
                type = "Flat",
                price = 200000.0,
                description = "Bel appartement de 8 pièces en centre-ville",//todo latin
                address = "456 Oak St, Anytown",
                status = PropertyStatus.SOLD,
                numberOfRooms = 8,
                numberOfBedrooms = 4,
                numberOfBathrooms = 2,
                surface = 70.0,
                pointOfInterest = "school, pharmacy, gym",
                entryDate = entryDate.time,
                saleDate = saleDate.time,
                latitude = 40.7128,
                longitude = -74.0060,
                photos = listOf(
                    Photo(
                        description = "Bedroom",
                        uri = "content://com.android.providers.media.documents/document/image%3A91011"
                    )
                )
            )
        )
    }
}