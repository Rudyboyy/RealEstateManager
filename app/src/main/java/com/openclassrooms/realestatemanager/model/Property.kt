package com.openclassrooms.realestatemanager.model

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.model.PropertyStatus
import com.openclassrooms.realestatemanager.utils.PhotoListConverter
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import java.util.*

@Entity(tableName = "realEstate")
@Parcelize
data class Property(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    val id: Long,
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
) : Parcelable {
    companion object : Parceler<Property> {
        override fun Property.write(parcel: Parcel, flags: Int) {
            parcel.writeLong(id)
            parcel.writeString(agent)
            parcel.writeString(type)
            parcel.writeDouble(price)
            parcel.writeString(description)
            parcel.writeString(address)
            parcel.writeString(status.name)
            parcel.writeInt(numberOfRooms)
            parcel.writeInt(numberOfBedrooms)
            parcel.writeInt(numberOfBathrooms)
            parcel.writeDouble(surface)
            parcel.writeString(pointOfInterest)
            parcel.writeLong(entryDate.time)
            parcel.writeLong(saleDate?.time ?: -1L)
            parcel.writeTypedList(photos)
            parcel.writeDouble(latitude ?: 0.0)
            parcel.writeDouble(longitude ?: 0.0)
        }

        override fun create(parcel: Parcel): Property {
            val id = parcel.readLong()
            val agent = parcel.readString() ?: ""
            val type = parcel.readString() ?: ""
            val price = parcel.readDouble()
            val description = parcel.readString() ?: ""
            val address = parcel.readString() ?: ""
            val status = PropertyStatus.valueOf(parcel.readString() ?: "")
            val numberOfRooms = parcel.readInt()
            val numberOfBedrooms = parcel.readInt()
            val numberOfBathrooms = parcel.readInt()
            val surface = parcel.readDouble()
            val pointOfInterest = parcel.readString() ?: ""
            val entryDate = Date(parcel.readLong())
            val saleDate = parcel.readLong().let { if (it != -1L) Date(it) else null }
            val photos = parcel.createTypedArrayList(Photo.CREATOR) ?: emptyList()
            val latitude = parcel.readDouble()
            val longitude = parcel.readDouble()

            return Property(
                id = id,
                agent = agent,
                type = type,
                price = price,
                description = description,
                address = address,
                status = status,
                numberOfRooms = numberOfRooms,
                numberOfBedrooms = numberOfBedrooms,
                numberOfBathrooms = numberOfBathrooms,
                surface = surface,
                pointOfInterest = pointOfInterest,
                entryDate = entryDate,
                saleDate = saleDate,
                photos = photos,
                latitude = latitude,
                longitude = longitude
            )
        }

        fun fromContentValues(values: ContentValues): Property {
            val id = values.getAsLong("property_id") ?: 0L
            val agent = values.getAsString("agent")
            val type = values.getAsString("type")
            val price = values.getAsDouble("price")
            val description = values.getAsString("description")
            val address = values.getAsString("address")
            val status = PropertyStatus.valueOf(values.getAsString("status"))
            val numberOfRooms = values.getAsInteger("number_of_rooms")
            val numberOfBedrooms = values.getAsInteger("number_of_bedrooms")
            val numberOfBathrooms = values.getAsInteger("number_of_bathrooms")
            val surface = values.getAsDouble("surface")
            val pointOfInterest = values.getAsString("point_of_interest")
            val entryDate = Date(values.getAsLong("entry_date"))
            val saleDate = values.getAsLong("sale_date")?.let { Date(it) }
            val latitude = values.getAsDouble("latitude")
            val longitude = values.getAsDouble("longitude")

            val photoListConverter = PhotoListConverter()
            val photoListString = values.getAsString("photos")
            val photoList = photoListConverter.toPhotoList(photoListString)

            return Property(
                id = id,
                agent = agent,
                type = type,
                price = price,
                description = description,
                address = address,
                status = status,
                numberOfRooms = numberOfRooms,
                numberOfBedrooms = numberOfBedrooms,
                numberOfBathrooms = numberOfBathrooms,
                surface = surface,
                pointOfInterest = pointOfInterest,
                entryDate = entryDate,
                saleDate = saleDate,
                photos = photoList,
                latitude = latitude,
                longitude = longitude
            )
        }
    }
}

