package com.screentimecontrol.android.di

import android.content.Context
import com.screentimecontrol.android.data.local.AppDatabase
import com.screentimecontrol.android.data.local.dao.AppLimitDao
import com.screentimecontrol.android.data.local.dao.OverrideLogDao
import com.screentimecontrol.android.data.local.dao.UsageSessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideAppLimitDao(database: AppDatabase): AppLimitDao {
        return database.appLimitDao()
    }

    @Provides
    fun provideUsageSessionDao(database: AppDatabase): UsageSessionDao {
        return database.usageSessionDao()
    }

    @Provides
    fun provideOverrideLogDao(database: AppDatabase): OverrideLogDao {
        return database.overrideLogDao()
    }
}