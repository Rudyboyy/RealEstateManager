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
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer tristique nibh et interdum consequat. Sed consequat, tortor vel rutrum congue, arcu ligula viverra nisi, sed gravida justo nisi vitae lectus. Pellentesque vel nulla eu lectus ultricies viverra ac sit amet orci. Duis finibus laoreet volutpat. In rhoncus tempor orci, eu viverra risus pretium a. Duis aliquam pretium ligula vel dictum. Donec eu felis turpis. Pellentesque a risus tincidunt, placerat velit id, dapibus odio. Sed porta purus vel convallis pretium. Nunc id risus ante. Vivamus in hendrerit sem. Duis non mi et lectus luctus ullamcorper. Curabitur placerat mauris quis elit ultrices consequat. Sed et sapien velit.",
                address = "123 Main St",
                city = "Anytown",
                postalCode = "14641",
                country = "United states",
                status = PropertyStatus.AVAILABLE,
                numberOfRooms = 6,
                numberOfBedrooms = 3,
                numberOfBathrooms = 1,
                surface = 150.0,
                pointOfInterest = "School, Supermarket, Park",
                entryDate = Date(System.currentTimeMillis()),
                latitude = 48.8566,
                longitude = 2.3522,
                photos = listOf(
                    Photo(
                        description = "Lounge",
                        uri = "https://images.unsplash.com/photo-1501183638710-841dd1904471?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
                    ),
                    Photo(
                        description = "Outside",
                        uri = "https://images.unsplash.com/photo-1556020685-ae41abfc9365?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
                    ),
                    Photo(
                        description = "Stair",
                        uri = "https://images.unsplash.com/photo-1502005229762-cf1b2da7c5d6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
                    ),
                    Photo(
                        description = "Kitchen",
                        uri = "https://images.unsplash.com/photo-1560440021-33f9b867899d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=659&q=80"
                    ),
                    Photo(
                        description = "Bedroom",
                        uri = "https://images.unsplash.com/photo-1556020685-ae41abfc9365?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
                    )
                )
            ),
            Property(
                id = 3,
                agent = "Bob Smith",
                type = "House",
                price = 350000.0,
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer tristique nibh et interdum consequat. Sed consequat, tortor vel rutrum congue, arcu ligula viverra nisi, sed gravida justo nisi vitae lectus. Pellentesque vel nulla eu lectus ultricies viverra ac sit amet orci. Duis finibus laoreet volutpat. In rhoncus tempor orci, eu viverra risus pretium a. Duis aliquam pretium ligula vel dictum. Donec eu felis turpis. Pellentesque a risus tincidunt, placerat velit id, dapibus odio. Sed porta purus vel convallis pretium. Nunc id risus ante. Vivamus in hendrerit sem. Duis non mi et lectus luctus ullamcorper. Curabitur placerat mauris quis elit ultrices consequat. Sed et sapien velit.",
                address = "123 Main St",
                city = "Anytown",
                postalCode = "14641",
                country = "United states",
                status = PropertyStatus.AVAILABLE,
                numberOfRooms = 6,
                numberOfBedrooms = 3,
                numberOfBathrooms = 1,
                surface = 150.0,
                pointOfInterest = "School, Supermarket, Park",
                entryDate = Date(System.currentTimeMillis()),
                latitude = 48.8566,
                longitude = 2.3522,
                photos = listOf(
                    Photo(
                        description = "Outside",
                        uri = "https://images.unsplash.com/photo-1556020685-ae41abfc9365?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
                    ),
                    Photo(
                        description = "Lounge",
                        uri = "https://images.unsplash.com/photo-1501183638710-841dd1904471?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
                    ),
                    Photo(
                        description = "Stair",
                        uri = "https://images.unsplash.com/photo-1502005229762-cf1b2da7c5d6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
                    ),
                    Photo(
                        description = "Kitchen",
                        uri = "https://images.unsplash.com/photo-1560440021-33f9b867899d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=659&q=80"
                    ),
                    Photo(
                        description = "Bedroom",
                        uri = "https://images.unsplash.com/photo-1556020685-ae41abfc9365?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
                    )
                )
            ),
            Property(
                id = 2,
                agent = "Jane Doe",
                type = "Flat",
                price = 200000.0,
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer tristique nibh et interdum consequat. Sed consequat, tortor vel rutrum congue, arcu ligula viverra nisi, sed gravida justo nisi vitae lectus. Pellentesque vel nulla eu lectus ultricies viverra ac sit amet orci. Duis finibus laoreet volutpat. In rhoncus tempor orci, eu viverra risus pretium a. Duis aliquam pretium ligula vel dictum. Donec eu felis turpis. Pellentesque a risus tincidunt, placerat velit id, dapibus odio. Sed porta purus vel convallis pretium. Nunc id risus ante. Vivamus in hendrerit sem. Duis non mi et lectus luctus ullamcorper. Curabitur placerat mauris quis elit ultrices consequat. Sed et sapien velit.",
                address = "456 Oak St",
                city = "Anytown",
                postalCode = "14641",
                country = "United states",
                status = PropertyStatus.SOLD,
                numberOfRooms = 8,
                numberOfBedrooms = 4,
                numberOfBathrooms = 2,
                surface = 70.0,
                pointOfInterest = "School, Pharmacy, Gas station",
                entryDate = entryDate.time,
                saleDate = saleDate.time,
                latitude = 40.7128,
                longitude = -74.0060,
                photos = listOf(
                    Photo(
                        description = "Lounge",
                        uri = "https://images.unsplash.com/photo-1503174971373-b1f69850bded?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1213&q=80"
                    ),
                    Photo(
                        description = "Outside",
                        uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1074&q=80"
                    ),Photo(
                        description = "Workspace",
                        uri = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1080&q=80"
                    ),Photo(
                        description = "Kitchen",
                        uri = "https://images.unsplash.com/photo-1502005097973-6a7082348e28?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
                    ),
                )
            )
        )
    }
}