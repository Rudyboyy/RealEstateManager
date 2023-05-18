package com.openclassrooms.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.model.PropertyStatus
import java.util.*

@Entity(tableName = "realEstate")
data class Property(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    val id: Int = 0,
    @ColumnInfo(name = "agent")
    val agent: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "status")
    val status: PropertyStatus,
    @ColumnInfo(name = "number_of_rooms")
    val numberOfRooms: Int,
    @ColumnInfo(name = "number_of_bedrooms")
    val numberOfBedrooms: Int,
    @ColumnInfo(name = "number_of_bathrooms")
    val numberOfBathrooms: Int,
    @ColumnInfo(name = "surface")
    val surface: Double,
    @ColumnInfo(name = "point_of_interest")
    val pointOfInterest: String,
    @ColumnInfo(name = "entry_date")
    val entryDate: Date,
    @ColumnInfo(name = "sale_date")
    val saleDate: Date? = null,
    @ColumnInfo(name = "photos")
    val photos: List<Photo>,
    @ColumnInfo(name = "latitude")
    val latitude: Double?,
    @ColumnInfo(name = "longitude")
    val longitude: Double?,
)

