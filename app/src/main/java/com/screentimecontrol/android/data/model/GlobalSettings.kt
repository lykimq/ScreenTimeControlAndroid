package com.screentimecontrol.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GlobalSettings(
    val globalDailyLimitMinutes: Int = 240, // 4 hours default
    val bedtimeStart: String = "22:00",
    val bedtimeEnd: String = "07:00",
    val workStart: String = "09:00",
    val workEnd: String = "17:00",
    val emergencyContacts: List<String> = emptyList(),
    val adminPin: String = "",
    val isParentalControlEnabled: Boolean = false
) : Parcelable