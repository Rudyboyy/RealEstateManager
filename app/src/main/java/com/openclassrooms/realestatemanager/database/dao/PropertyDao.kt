package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.room.*
import com.openclassrooms.realestatemanager.model.Property
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Query("SELECT * FROM realEstate")
    fun getAllProperties(): Flow<List<Property>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(property: Property)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(properties: List<Property>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(property: Property): Int

    @Query("SELECT * FROM realEstate WHERE property_id = :propertyId")
    fun getPropertiesWithCursor(propertyId: Long): Cursor

    @Query("SELECT * FROM realEstate")
    fun getPropertiesWithCursor(): Cursor

    @Query(
        "SELECT * FROM realEstate WHERE " +
                "(:minPrice IS NULL OR price >= :minPrice) AND " +
                "(:maxPrice IS NULL OR price <= :maxPrice) AND " +
                "(:minSurface IS NULL OR surface >= :minSurface) AND " +
                "(:maxSurface IS NULL OR surface <= :maxSurface) AND " +
                "(:poiList = '' OR point_of_interest LIKE '%' || :poiList || '%')"
    )
    fun getFilteredProperties(
        minPrice: Int?,
        maxPrice: Int?,
        minSurface: Int?,
        maxSurface: Int?,
        poiList: String?
    ): List<Property>
}
