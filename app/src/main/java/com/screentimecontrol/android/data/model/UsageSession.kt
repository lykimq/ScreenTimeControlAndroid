package com.screentimecontrol.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsageSession(
    val packageName: String,
    val startTime: Long,
    val endTime: Long,
    val durationMinutes: Int
) : Parcelable