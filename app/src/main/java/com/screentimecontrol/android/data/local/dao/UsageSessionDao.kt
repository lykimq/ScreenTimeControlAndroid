package com.screentimecontrol.android.data.local.dao

import androidx.room.*
import com.screentimecontrol.android.data.local.entity.UsageSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageSessionDao {
    @Query("SELECT * FROM usage_sessions WHERE date = :date")
    fun getUsageSessionsByDate(date: String): Flow<List<UsageSessionEntity>>

    @Query("SELECT * FROM usage_sessions WHERE packageName = :packageName AND date = :date")
    fun getUsageSessionsByPackageAndDate(packageName: String, date: String): Flow<List<UsageSessionEntity>>

    @Query("SELECT SUM(durationMinutes) FROM usage_sessions WHERE packageName = :packageName AND date = :date")
    suspend fun getTotalUsageByPackageAndDate(packageName: String, date: String): Int?

    @Query("SELECT * FROM usage_sessions WHERE date = :date ORDER BY startTime DESC")
    fun getUsageSessionsByDateOrdered(date: String): Flow<List<UsageSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageSession(usageSession: UsageSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageSessions(usageSessions: List<UsageSessionEntity>)

    @Update
    suspend fun updateUsageSession(usageSession: UsageSessionEntity)

    @Delete
    suspend fun deleteUsageSession(usageSession: UsageSessionEntity)

    @Query("DELETE FROM usage_sessions WHERE date < :date")
    suspend fun deleteOldUsageSessions(date: String)

    @Query("DELETE FROM usage_sessions WHERE packageName = :packageName AND date = :date")
    suspend fun deleteUsageSessionsByPackageAndDate(packageName: String, date: String)
}