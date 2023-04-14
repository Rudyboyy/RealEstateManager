package com.openclassrooms.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "realEstate")
data class RealEstate(
    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "property_id")
     val id: Int,
//    @ColumnInfo(index = true) val userId: Long,
    @ColumnInfo(name = "property_type") val propertyType: String
//    val price: Double,
//    val address: String,
)