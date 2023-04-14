package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.model.RealEstate
import kotlinx.coroutines.flow.Flow

class RealEstateRepository(private val realEstateDao: RealEstateDao) {

    val allProperties: Flow<List<RealEstate>> = realEstateDao.getAllRealEstates()

    suspend fun invoke(realEstateEntity: RealEstate) {
        realEstateDao.insert(realEstateEntity)
    }

    suspend fun update(realEstate: RealEstate): Int {
        return realEstateDao.update(realEstate)
    }
}