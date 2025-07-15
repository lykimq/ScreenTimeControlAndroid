package com.screentimecontrol.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.screentimecontrol.android.data.model.UsageSession

@Entity(tableName = "usage_sessions")
data class UsageSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val startTime: Long,
    val endTime: Long,
    val durationMinutes: Int,
    val date: String
) {
    fun toUsageSession(): UsageSession {
        return UsageSession(
            packageName = packageName,
            startTime = startTime,
            endTime = endTime,
            durationMinutes = durationMinutes
        )
    }

    companion object {
        fun fromUsageSession(usageSession: UsageSession, date: String): UsageSessionEntity {
            return UsageSessionEntity(
                packageName = usageSession.packageName,
                startTime = usageSession.startTime,
                endTime = usageSession.endTime,
                durationMinutes = usageSession.durationMinutes,
                date = date
            )
        }
    }
}