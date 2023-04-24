package com.openclassrooms.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

@Entity(tableName = "realEstate")
data class Property(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    val id: Int,
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
    @ColumnInfo(name = "surface")
    val surface: Double,
    @ColumnInfo(name = "point_of_interest")
    val pointOfInterest: String,
    @ColumnInfo(name = "entry_date")
    val entryDate: LocalDate,
    @ColumnInfo(name = "sale_date")
    val saleDate: LocalDate? = null,
    @ColumnInfo(name = "photos")
    val photos: List<Photo>,
)

