package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.PropertyStatus
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

class DummyRealEstateProvider {

    companion object {
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
                surface = 150.0,
                pointOfInterest = "school, grocery store, park",
                entryDate = LocalDate.now().minusMonths(3),
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
                description = "Bel appartement de 3 pièces en centre-ville",//todo latin
                address = "456 Oak St, Anytown",
                status = PropertyStatus.SOLD,
                numberOfRooms = 3,
                surface = 70.0,
                pointOfInterest = "school, pharmacy, gym",
                entryDate = LocalDate.of(2022, Month.JANUARY, 1),
                saleDate = LocalDate.of(2022, Month.FEBRUARY, 15),
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