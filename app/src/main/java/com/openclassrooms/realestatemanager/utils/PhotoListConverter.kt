package com.openclassrooms.realestatemanager.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.model.Photo

class PhotoListConverter {
    @TypeConverter
    fun fromPhotoList(photoList: List<Photo>): String {
        val gson = Gson()
        return gson.toJson(photoList)
    }

    @TypeConverter
    fun toPhotoList(photoListString: String): List<Photo> {
        val gson = Gson()
        val type = object : TypeToken<List<Photo>>() {}.type
        return gson.fromJson(photoListString, type)
    }
}