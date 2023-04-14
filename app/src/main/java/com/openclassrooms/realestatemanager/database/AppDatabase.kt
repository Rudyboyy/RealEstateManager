package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.utils.DummyRealEstateProvider
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [Property::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun realEstateDao(): PropertyDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val NUMBER_OF_THREADS = 4

        val databaseWriteExecutor: ExecutorService =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "realEstate_database"
                )
                    .addCallback(mRoomDatabaseCallBack)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val mRoomDatabaseCallBack = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriteExecutor.execute {
                    INSTANCE?.let {
                        val realEstateDao = it.realEstateDao()
                        realEstateDao.insertAll(DummyRealEstateProvider.samplePropertyList)
                    }
                }
            }
        }
    }
}

