package com.openclassrooms.realestatemanager.utils

import android.content.ContentValues
import android.database.Cursor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.PropertyStatus
import java.util.*

object UtilsForTest {

    fun generateProperty(): ContentValues {
        val property = DummyRealEstateProvider.samplePropertyList[0]
        val photosJson = Gson().toJson(property.photos)
        val values = ContentValues().apply {
            put("property_id", property.id)
            put("agent", property.agent)
            put("type", property.type)
            put("price", property.price)
            put("description", property.description)
            put("address", property.address)
            put("city", property.city)
            put("postal_code", property.postalCode)
            put("country", property.country)
            put("status", property.status.name)
            put("number_of_rooms", property.numberOfRooms)
            put("number_of_bedrooms", property.numberOfBedrooms)
            put("number_of_bathrooms", property.numberOfBathrooms)
            put("surface", property.surface)
            put("point_of_interest", property.pointOfInterest)
            put("entry_date", property.entryDate.time)
            put("sale_date", property.saleDate?.time)
            put("latitude", property.latitude)
            put("longitude", property.longitude)
            put("photos", photosJson)
        }
        return values
    }

    fun getPropertyFromCursor(cursor: Cursor): Property {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow("property_id"))
        val agent = cursor.getString(cursor.getColumnIndexOrThrow("agent"))
        val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
        val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
        val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
        val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
        val city = cursor.getString(cursor.getColumnIndexOrThrow("city"))
        val postalCode = cursor.getString(cursor.getColumnIndexOrThrow("postal_code"))
        val country = cursor.getString(cursor.getColumnIndexOrThrow("country"))
        val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
        val numberOfRooms = cursor.getInt(cursor.getColumnIndexOrThrow("number_of_rooms"))
        val numberOfBedrooms = cursor.getInt(cursor.getColumnIndexOrThrow("number_of_bedrooms"))
        val numberOfBathrooms = cursor.getInt(cursor.getColumnIndexOrThrow("number_of_bathrooms"))
        val surface = cursor.getDouble(cursor.getColumnIndexOrThrow("surface"))
        val pointOfInterest = cursor.getString(cursor.getColumnIndexOrThrow("point_of_interest"))
        val entryDate = Date(cursor.getLong(cursor.getColumnIndexOrThrow("entry_date")))
        val saleDate = Date(cursor.getLong(cursor.getColumnIndexOrThrow("sale_date")))
        val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"))
        val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))
        val photosJson = cursor.getString(cursor.getColumnIndexOrThrow("photos"))
        val photos = Gson().fromJson<List<Photo>>(
            photosJson,
            object : TypeToken<List<Photo>>() {}.type
        )

        return Property(
            id = id,
            agent = agent,
            type = type,
            price = price,
            description = description,
            address = address,
            city = city,
            postalCode = postalCode,
            country = country,
            status = PropertyStatus.valueOf(status),
            numberOfRooms = numberOfRooms,
            numberOfBedrooms = numberOfBedrooms,
            numberOfBathrooms = numberOfBathrooms,
            surface = surface,
            pointOfInterest = pointOfInterest,
            entryDate = entryDate,
            saleDate = saleDate,
            latitude = latitude,
            longitude = longitude,
            photos = photos
        )
    }

    fun generateProperties(): List<ContentValues> {
        val properties = DummyRealEstateProvider.samplePropertyList
        val propertyValuesList = mutableListOf<ContentValues>()

        for (property in properties) {
            val photosJson = Gson().toJson(property.photos)
            val values = ContentValues().apply {
                put("property_id", property.id)
                put("agent", property.agent)
                put("type", property.type)
                put("price", property.price)
                put("description", property.description)
                put("address", property.address)
                put("city", property.city)
                put("postal_code", property.postalCode)
                put("country", property.country)
                put("status", property.status.name)
                put("number_of_rooms", property.numberOfRooms)
                put("number_of_bedrooms", property.numberOfBedrooms)
                put("number_of_bathrooms", property.numberOfBathrooms)
                put("surface", property.surface)
                put("point_of_interest", property.pointOfInterest)
                put("entry_date", property.entryDate.time)
                put("sale_date", property.saleDate?.time)
                put("latitude", property.latitude)
                put("longitude", property.longitude)
                put("photos", photosJson)
            }
            propertyValuesList.add(values)
        }

        return propertyValuesList
    }

    fun generateProperties(property: Property): ContentValues {
        val photosJson = Gson().toJson(property.photos)
        val values = ContentValues().apply {
            put("property_id", property.id)
            put("agent", property.agent)
            put("type", property.type)
            put("price", property.price)
            put("description", property.description)
            put("address", property.address)
            put("city", property.city)
            put("postal_code", property.postalCode)
            put("country", property.country)
            put("status", property.status.name)
            put("number_of_rooms", property.numberOfRooms)
            put("number_of_bedrooms", property.numberOfBedrooms)
            put("number_of_bathrooms", property.numberOfBathrooms)
            put("surface", property.surface)
            put("point_of_interest", property.pointOfInterest)
            put("entry_date", property.entryDate.time)
            put("sale_date", property.saleDate?.time)
            put("latitude", property.latitude)
            put("longitude", property.longitude)
            put("photos", photosJson)
        }
        return values
    }
}