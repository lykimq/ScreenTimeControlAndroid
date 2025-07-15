package com.screentimecontrol.android.data.local.dao

import androidx.room.*
import com.screentimecontrol.android.data.local.entity.AppLimitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppLimitDao {
    @Query("SELECT * FROM app_limits")
    fun getAllAppLimits(): Flow<List<AppLimitEntity>>

    @Query("SELECT * FROM app_limits WHERE packageName = :packageName")
    suspend fun getAppLimitByPackage(packageName: String): AppLimitEntity?

    @Query("SELECT * FROM app_limits WHERE isWhitelisted = 1")
    fun getWhitelistedApps(): Flow<List<AppLimitEntity>>

    @Query("SELECT * FROM app_limits WHERE isEducational = 1")
    fun getEducationalApps(): Flow<List<AppLimitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppLimit(appLimit: AppLimitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppLimits(appLimits: List<AppLimitEntity>)

    @Update
    suspend fun updateAppLimit(appLimit: AppLimitEntity)

    @Delete
    suspend fun deleteAppLimit(appLimit: AppLimitEntity)

    @Query("DELETE FROM app_limits WHERE packageName = :packageName")
    suspend fun deleteAppLimitByPackage(packageName: String)

    @Query("DELETE FROM app_limits")
    suspend fun deleteAllAppLimits()
}