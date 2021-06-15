package com.example.weatherappcode.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weatherappcode.data.db.dao.CachedDataDao
import com.example.weatherappcode.data.db.dao.SavedCityDao
import com.example.weatherappcode.data.db.entity.CachedData
import com.example.weatherappcode.data.db.entity.SavedCity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [CachedData::class, SavedCity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract fun cachedDataDao(): CachedDataDao
    abstract fun savedCityDao(): SavedCityDao


    private class DatabaseCallback(private val scope: CoroutineScope) :
        Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDB? = null
        fun getInstance(context: Context, scope: CoroutineScope): AppDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDB::class.java,
                        "weather_db"
                    )
                        .addCallback(DatabaseCallback(scope))
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}