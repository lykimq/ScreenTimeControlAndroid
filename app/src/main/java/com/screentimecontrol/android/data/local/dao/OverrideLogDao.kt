package com.screentimecontrol.android.data.local.dao

import androidx.room.*
import com.screentimecontrol.android.data.local.entity.OverrideLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OverrideLogDao {
    @Query("SELECT * FROM override_logs ORDER BY timestamp DESC")
    fun getAllOverrideLogs(): Flow<List<OverrideLogEntity>>

    @Query("SELECT * FROM override_logs WHERE packageName = :packageName ORDER BY timestamp DESC")
    fun getOverrideLogsByPackage(packageName: String): Flow<List<OverrideLogEntity>>

    @Query("SELECT * FROM override_logs WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    fun getOverrideLogsByTimeRange(startTime: Long, endTime: Long): Flow<List<OverrideLogEntity>>

    @Insert
    suspend fun insertOverrideLog(overrideLog: OverrideLogEntity)

    @Insert
    suspend fun insertOverrideLogs(overrideLogs: List<OverrideLogEntity>)

    @Delete
    suspend fun deleteOverrideLog(overrideLog: OverrideLogEntity)

    @Query("DELETE FROM override_logs WHERE timestamp < :timestamp")
    suspend fun deleteOldOverrideLogs(timestamp: Long)
}