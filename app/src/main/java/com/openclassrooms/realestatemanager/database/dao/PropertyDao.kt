package com.openclassrooms.realestatemanager.database.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.model.Property
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Query("SELECT * FROM realEstate")
    fun getAllProperties(): Flow<List<Property>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(property: Property): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(properties: List<Property>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(property: Property)
}