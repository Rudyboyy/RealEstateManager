package com.openclassrooms.realestatemanager.injection

import android.content.Context
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    private fun provideRealEstateRepository(context: Context): RealEstateRepository {
        val database = AppDatabase.getDatabase(context)
        return RealEstateRepository(database.realEstateDao())
    }

    private fun provideExecutor(): Executor = Executors.newSingleThreadExecutor()

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val realEstateRepository = provideRealEstateRepository(context)
        val executor = provideExecutor()
        return ViewModelFactory(realEstateRepository, executor)
    }

}