package com.screentimecontrol.android.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.screentimecontrol.android.data.local.dao.AppLimitDao
import com.screentimecontrol.android.data.local.dao.OverrideLogDao
import com.screentimecontrol.android.data.local.dao.UsageSessionDao
import com.screentimecontrol.android.data.local.entity.AppLimitEntity
import com.screentimecontrol.android.data.local.entity.OverrideLogEntity
import com.screentimecontrol.android.data.local.entity.UsageSessionEntity

@Database(
    entities = [
        AppLimitEntity::class,
        UsageSessionEntity::class,
        OverrideLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appLimitDao(): AppLimitDao
    abstract fun usageSessionDao(): UsageSessionDao
    abstract fun overrideLogDao(): OverrideLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "screen_time_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}