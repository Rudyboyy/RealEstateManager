package com.openclassrooms.realestatemanager.injection

import android.content.Context
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.service.GeocodingApiService
import com.openclassrooms.realestatemanager.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    private fun providePropertyDao(context: Context): PropertyDao {
        val database = AppDatabase.getDatabase(context)
        return database.realEstateDao()
    }

    private fun provideGeocodingApiService(): GeocodingApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingApiService::class.java)
    }

    private fun provideRealEstateRepository(context: Context): RealEstateRepository {
        val propertyDao = providePropertyDao(context)
        val geocodingApiService = provideGeocodingApiService()
        return RealEstateRepository(propertyDao, geocodingApiService)
    }

    private fun provideExecutor(): Executor = Executors.newSingleThreadExecutor()

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val realEstateRepository = provideRealEstateRepository(context)
        val executor = provideExecutor()
        return ViewModelFactory(realEstateRepository, executor)
    }
}