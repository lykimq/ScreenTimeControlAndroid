package com.screentimecontrol.android.manager

import android.content.Context
import android.content.Intent
import com.screentimecontrol.android.data.local.dao.OverrideLogDao
import com.screentimecontrol.android.data.local.dao.UsageSessionDao
import com.screentimecontrol.android.data.local.entity.OverrideLogEntity
import com.screentimecontrol.android.service.OverlayService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val usageSessionDao: UsageSessionDao,
    private val overrideLogDao: OverrideLogDao
) {

    fun checkAndEnforceLimits(packageName: String, appName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val totalUsage = usageSessionDao.getTotalUsageByPackageAndDate(packageName, today) ?: 0

            // TODO: Get app limit from database
            val appLimit = 60 // Default 60 minutes

            if (totalUsage >= appLimit) {
                showBlockingOverlay(packageName, appName, 0)
            }
        }
    }

    private fun showBlockingOverlay(packageName: String, appName: String, remainingTime: Int) {
        val intent = Intent(context, OverlayService::class.java).apply {
            putExtra("package_name", packageName)
            putExtra("app_name", appName)
            putExtra("remaining_time", remainingTime)
        }
        context.startService(intent)
    }

    fun logOverride(packageName: String, reason: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val overrideLog = OverrideLogEntity(
                timestamp = System.currentTimeMillis(),
                packageName = packageName,
                reason = reason,
                durationMinutes = 0
            )
            overrideLogDao.insertOverrideLog(overrideLog)
        }
    }

    fun getTodayUsage(packageName: String): Int {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return runBlocking {
            usageSessionDao.getTotalUsageByPackageAndDate(packageName, today) ?: 0
        }
    }
}