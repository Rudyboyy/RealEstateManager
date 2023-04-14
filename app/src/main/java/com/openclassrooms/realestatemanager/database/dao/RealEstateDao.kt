package com.openclassrooms.realestatemanager.database.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.model.RealEstate
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEstateDao {

    @Query("SELECT * FROM realEstate")
    fun getAllRealEstates(): Flow<List<RealEstate>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(property: RealEstate): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(realEstates: List<RealEstate>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(realEstate: RealEstate): Int
}