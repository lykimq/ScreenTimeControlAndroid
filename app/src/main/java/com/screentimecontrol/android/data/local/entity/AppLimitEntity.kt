package com.screentimecontrol.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.screentimecontrol.android.data.model.AppCategory

@Entity(tableName = "app_limits")
data class AppLimitEntity(
    @PrimaryKey val packageName: String,
    val appName: String,
    val dailyLimitMinutes: Int,
    val category: String,
    val isWhitelisted: Boolean,
    val isEducational: Boolean
) {
    fun toAppLimit(): com.screentimecontrol.android.data.model.AppLimit {
        return com.screentimecontrol.android.data.model.AppLimit(
            packageName = packageName,
            appName = appName,
            dailyLimitMinutes = dailyLimitMinutes,
            category = AppCategory.valueOf(category),
            isWhitelisted = isWhitelisted,
            isEducational = isEducational
        )
    }

    companion object {
        fun fromAppLimit(appLimit: com.screentimecontrol.android.data.model.AppLimit): AppLimitEntity {
            return AppLimitEntity(
                packageName = appLimit.packageName,
                appName = appLimit.appName,
                dailyLimitMinutes = appLimit.dailyLimitMinutes,
                category = appLimit.category.name,
                isWhitelisted = appLimit.isWhitelisted,
                isEducational = appLimit.isEducational
            )
        }
    }
}