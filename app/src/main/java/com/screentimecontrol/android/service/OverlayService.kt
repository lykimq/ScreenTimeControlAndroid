package com.screentimecontrol.android.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.screentimecontrol.android.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverlayService : Service() {

    @Inject
    lateinit var usageManager: UsageManager

    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null

    companion object {
        private const val OVERLAY_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val packageName = intent?.getStringExtra("package_name")
        val appName = intent?.getStringExtra("app_name")
        val remainingTime = intent?.getIntExtra("remaining_time", 0) ?: 0

        if (packageName != null) {
            showBlockingOverlay(packageName, appName ?: packageName, remainingTime)
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun showBlockingOverlay(packageName: String, appName: String, remainingTime: Int) {
        val layoutParams = WindowManager.LayoutParams().apply {
            type = OVERLAY_TYPE
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            gravity = Gravity.CENTER
        }

        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_blocking_screen, null).apply {
            findViewById<TextView>(R.id.tvAppName).text = appName
            findViewById<TextView>(R.id.tvMessage).text = "You've reached your daily limit for this app"

            if (remainingTime > 0) {
                findViewById<TextView>(R.id.tvRemainingTime).text =
                    "You can use this app again in ${remainingTime} minutes"
            } else {
                findViewById<TextView>(R.id.tvRemainingTime).text =
                    "You can use this app again tomorrow"
            }

            findViewById<Button>(R.id.btnEmergency).setOnClickListener {
                handleEmergencyOverride(packageName)
            }

            findViewById<Button>(R.id.btnClose).setOnClickListener {
                hideOverlay()
            }
        }

        try {
            windowManager.addView(overlayView, layoutParams)
        } catch (e: Exception) {
            // Handle permission denied or other errors
        }
    }

    private fun handleEmergencyOverride(packageName: String) {
        // TODO: Implement biometric authentication
        // For now, just log the override
        usageManager.logOverride(packageName, "Emergency override")
        hideOverlay()
    }

    private fun hideOverlay() {
        overlayView?.let { view ->
            try {
                windowManager.removeView(view)
            } catch (e: Exception) {
                // Handle error
            }
            overlayView = null
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideOverlay()
    }
}