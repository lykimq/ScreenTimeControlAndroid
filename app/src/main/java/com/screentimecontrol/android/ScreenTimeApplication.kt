package com.screentimecontrol.android

// Purpose: Application entry point with dependency injection setup
// This is the main entry point for the application.
// It is used to initialize the application and set up the dependency injection.
// It is also used to set up the work manager configuration.

// Import statements
import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

// The @HiltAndroidApp annotation is used to initialize the application and set up the dependency injection.
@HiltAndroidApp

// The ScreenTimeApplication class is used to initialize the application and set up the dependency injection.
// The Application class is used to initialize the application and set up the dependency injection.
// The Configuration.Provider interface is used to set up the work manager configuration.

class ScreenTimeApplication : Application(), Configuration.Provider {

    // The @Inject annotation is used to inject the worker factory.
    @Inject
    // The lateinit var is used to initialize the worker factory.
    lateinit var workerFactory: HiltWorkerFactory

    // The getWorkManagerConfiguration() method is used to get the work manager configuration.
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}