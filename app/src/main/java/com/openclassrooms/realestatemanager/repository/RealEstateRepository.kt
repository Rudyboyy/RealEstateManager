package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property
import kotlinx.coroutines.flow.Flow

class RealEstateRepository(private val propertyDao: PropertyDao) {

    val properties: Flow<List<Property>> = propertyDao.getAllProperties()

    suspend fun invoke(propertyEntity: Property) {
        propertyDao.insert(propertyEntity)
    }

    suspend fun update(property: Property) {
        propertyDao.update(property)
    }
}