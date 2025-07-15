package com.screentimecontrol.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppLimit(
    val packageName: String,
    val appName: String,
    val dailyLimitMinutes: Int,
    val category: AppCategory,
    val isWhitelisted: Boolean = false,
    val isEducational: Boolean = false
) : Parcelable

enum class AppCategory(val displayName: String) {
    SOCIAL_MEDIA("Social Media"),
    ENTERTAINMENT("Entertainment"),
    PRODUCTIVITY("Productivity"),
    EDUCATION("Education"),
    GAMING("Gaming"),
    UTILITIES("Utilities"),
    SYSTEM("System"),
    OTHER("Other")
}