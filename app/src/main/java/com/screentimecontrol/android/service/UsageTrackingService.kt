package com.screentimecontrol.android.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.screentimecontrol.android.R
import com.screentimecontrol.android.data.local.dao.UsageSessionDao
import com.screentimecontrol.android.data.local.entity.UsageSessionEntity
import com.screentimecontrol.android.data.model.UsageSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UsageTrackingService : Service() {

    @Inject
    lateinit var usageSessionDao: UsageSessionDao

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var currentSession: UsageSession? = null
    private var lastForegroundApp: String? = null

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "usage_tracking_channel"
        private const val TRACKING_INTERVAL = 5000L // 5 seconds
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        startUsageTracking()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        endCurrentSession()
    }

    private fun startUsageTracking() {
        serviceScope.launch {
            while (isActive) {
                trackCurrentAppUsage()
                delay(TRACKING_INTERVAL)
            }
        }
    }

    private suspend fun trackCurrentAppUsage() {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val endTime = currentTime
        val startTime = currentTime - TRACKING_INTERVAL

        val usageEvents = usageStatsManager.queryEvents(startTime, endTime)
        val event = UsageEvents.Event()
        var foregroundApp: String? = null

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                foregroundApp = event.packageName
            }
        }

        if (foregroundApp != null && foregroundApp != lastForegroundApp) {
            // App changed, end current session and start new one
            endCurrentSession()
            startNewSession(foregroundApp, currentTime)
            lastForegroundApp = foregroundApp
        } else if (foregroundApp != null && currentSession != null) {
            // Update current session duration
            currentSession = currentSession?.copy(
                endTime = currentTime,
                durationMinutes = ((currentTime - currentSession!!.startTime) / 60000).toInt()
            )
        }
    }

    private fun startNewSession(packageName: String, startTime: Long) {
        currentSession = UsageSession(
            packageName = packageName,
            startTime = startTime,
            endTime = startTime,
            durationMinutes = 0
        )
    }

    private suspend fun endCurrentSession() {
        currentSession?.let { session ->
            if (session.durationMinutes > 0) {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val sessionEntity = UsageSessionEntity.fromUsageSession(session, date)
                usageSessionDao.insertUsageSession(sessionEntity)
            }
            currentSession = null
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Usage Tracking",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Tracks app usage for screen time control"
                setShowBadge(false)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Screen Time Control")
            .setContentText("Tracking app usage")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
}