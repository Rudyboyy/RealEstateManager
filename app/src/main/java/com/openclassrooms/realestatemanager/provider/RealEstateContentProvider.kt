package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property
import kotlinx.coroutines.*

class RealEstateContentProvider : ContentProvider() {

    private lateinit var propertyDao: PropertyDao

    companion object {
        private const val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
        private val TABLE_NAME = Property::class.simpleName
        val URI_ITEM: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }


    override fun onCreate(): Boolean {
        context?.let {
            val database = AppDatabase.getDatabase(it)
            propertyDao = database.realEstateDao()
            return true
        }
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val propertyId = ContentUris.parseId(uri)
        val cursor = propertyDao.getPropertiesWithCursor(propertyId)
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        values?.let {
            val property = Property.fromContentValues(values)
            CoroutineScope(Dispatchers.IO).launch {
                propertyDao.insert(property)
            }
            context?.contentResolver?.notifyChange(uri, null)
            return ContentUris.withAppendedId(uri, property.id)
        }
        throw IllegalArgumentException("Failed to insert row for uri $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        values?.let {
            val property = Property.fromContentValues(values)
            var count = 0
            runBlocking {
                withContext(Dispatchers.IO) {
                    count = propertyDao.update(property)
                }
            }
            context?.contentResolver?.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to update row for uri $uri")
    }
}
