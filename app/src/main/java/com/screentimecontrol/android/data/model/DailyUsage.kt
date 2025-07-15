package com.screentimecontrol.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyUsage(
    val date: String, // YYYY-MM-DD format
    val packageName: String,
    val totalMinutes: Int,
    val sessions: List<UsageSession>
) : Parcelable