package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris.withAppendedId
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.PropertyStatus
import com.openclassrooms.realestatemanager.provider.RealEstateContentProvider
import com.openclassrooms.realestatemanager.utils.DummyRealEstateProvider
import com.openclassrooms.realestatemanager.utils.UtilsForTest.generateProperty
import com.openclassrooms.realestatemanager.utils.UtilsForTest.getPropertyFromCursor
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ContentProviderTest {

    private lateinit var contentResolver: ContentResolver
    private val uri: Uri = RealEstateContentProvider.URI_ITEM
    private val nonExistingPropertyId = -1
    private val propertyID = 1

    @Before
    fun setup() {
        contentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @Test
    fun insertPropertyWithSuccess() {
        val values = generateProperty()
        val resultUri = contentResolver.insert(uri, values)
        assertNotNull(resultUri)

        val cursor = contentResolver.query(
            withAppendedId(
                RealEstateContentProvider.URI_ITEM,
                propertyID.toLong()
            ), null, null, null, null
        )

        assertNotNull(cursor)
        assertEquals(1, cursor?.count)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val insertedProperty = getPropertyFromCursor(cursor)
                val matchingProperty = propertyID.toLong() == insertedProperty.id
                assertNotNull(matchingProperty)
            } while (cursor.moveToNext())
        }

        cursor?.close()
    }

    @Test
    fun getFirstPropertyOfTableIfExists() {
        val cursor =
            contentResolver.query(withAppendedId(uri, propertyID.toLong()), null, null, null, null)
        if (cursor!!.count == 1) {
            Assert.assertTrue(cursor.moveToFirst())
            Assert.assertEquals(
                propertyID,
                cursor.getInt(cursor.getColumnIndexOrThrow("property_id"))
            )
        }
        cursor.close()
    }

    @Test
    fun whenFetchingNonExistingPropertyThenCursorIsEmpty() {
        val cursor = contentResolver.query(
            withAppendedId(uri, nonExistingPropertyId.toLong()),
            null,
            null,
            null,
            null
        )
        Assert.assertNotNull(cursor)
        Assert.assertTrue(cursor!!.count == 0)
        cursor.close()
    }
}