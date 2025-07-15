# Screen Time Control Android - Code Documentation

## 📚 Introduction

This document explains the codebase structure, Kotlin concepts, and how each component works together. It's designed for developers new to Kotlin and Android development.

## 🏗️ Project Structure Overview

```
ScreenTimeControlAndroid/
├── app/                          # Main Android application module
│   ├── build.gradle.kts         # App-level dependencies and configuration
│   └── src/main/
│       ├── AndroidManifest.xml  # App permissions and component declarations
│       ├── java/com/screentimecontrol/android/
│       │   ├── data/            # Data layer (database, models)
│       │   ├── service/         # Background services
│       │   ├── ui/              # User interface (Jetpack Compose)
│       │   ├── manager/         # Business logic
│       │   ├── di/              # Dependency injection
│       │   └── ScreenTimeApplication.kt  # Main application class
│       └── res/                 # Resources (strings, layouts, themes)
├── build.gradle.kts             # Project-level configuration
├── settings.gradle.kts          # Project settings
└── gradle/wrapper/              # Gradle wrapper configuration
```

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (latest version)
- **Java Development Kit (JDK)** 11 or higher
- **Android SDK** API level 26+ (Android 8.0+)

### Installation Steps

1. **Install Android Studio**
   ```bash
   # Download from: https://developer.android.com/studio
   ```

2. **Open the Project**
   - Launch Android Studio
   - Click "Open an existing Android Studio project"
   - Navigate to `ScreenTimeControlAndroid` folder
   - Click "OK"

3. **Wait for Gradle Sync**
   - Android Studio will automatically download dependencies
   - This may take 5-10 minutes on first run
   - Watch the progress bar at the bottom

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click the green "Run" button (▶️) in the toolbar
   - Select your device and click "OK"

## 📦 Dependencies Explained

### What are Dependencies?
Dependencies are external libraries that provide pre-built functionality. Instead of writing everything from scratch, we use these libraries.

### Key Dependencies in `app/build.gradle.kts`:

```kotlin
// Core Android libraries
implementation("androidx.core:core-ktx:1.12.0")           // Kotlin extensions for Android
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")  // Lifecycle management

// UI Framework
implementation("androidx.compose.ui:ui")                   // Jetpack Compose UI components
implementation("androidx.compose.material3:material3")     // Material Design 3 components

// Database
implementation("androidx.room:room-runtime:2.6.1")        // Room database
implementation("androidx.room:room-ktx:2.6.1")            // Kotlin extensions for Room

// Dependency Injection
implementation("com.google.dagger:hilt-android:2.48")     // Hilt for dependency injection

// Background Processing
implementation("androidx.work:work-runtime-ktx:2.9.0")    // WorkManager for background tasks

// Biometric Authentication
implementation("androidx.biometric:biometric:1.1.0")     // Fingerprint/face recognition
```

## 🗂️ File-by-File Explanation

### 📁 Root Level Files

#### `build.gradle.kts` (Project Level)
**Purpose**: Configures the entire project
```kotlin
plugins {
    id("com.android.application") version "8.2.2" apply false  // Android build tools
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false  // Kotlin compiler
    id("com.google.dagger.hilt.android") version "2.48" apply false  // Hilt DI
}
```

#### `settings.gradle.kts`
**Purpose**: Defines project structure and repositories
```kotlin
rootProject.name = "ScreenTimeControlAndroid"  // Project name
include(":app")  // Include the app module
```

#### `app/build.gradle.kts` (App Level)
**Purpose**: Configures the Android app specifically
```kotlin
android {
    namespace = "com.screentimecontrol.android"  // Package name
    compileSdk = 34  // Target Android API level
    defaultConfig {
        applicationId = "com.screentimecontrol.android"  // App ID on Play Store
        minSdk = 26  // Minimum Android version supported
        targetSdk = 34  // Target Android version
    }
}
```

### 📁 Application Entry Point

#### `ScreenTimeApplication.kt`
**Purpose**: Main application class that initializes the app
```kotlin
@HiltAndroidApp  // Enables Hilt dependency injection
class ScreenTimeApplication : Application(), Configuration.Provider {
    // This class runs when the app starts
    // It sets up WorkManager for background tasks
}
```

**What it does:**
- Initializes Hilt dependency injection
- Configures WorkManager for background processing
- Runs before any activities or services

### 📁 Data Layer (`data/`)

The data layer handles all data storage and retrieval.

#### `data/model/` - Data Classes
**Purpose**: Define the structure of data objects

**`AppLimit.kt`**
```kotlin
@Parcelize  // Allows passing between screens
data class AppLimit(
    val packageName: String,      // App's unique identifier
    val appName: String,          // Human-readable app name
    val dailyLimitMinutes: Int,   // Time limit in minutes
    val category: AppCategory,    // App category (Social, Gaming, etc.)
    val isWhitelisted: Boolean,   // Always allowed?
    val isEducational: Boolean    // Educational app?
)
```

**`UsageSession.kt`**
```kotlin
data class UsageSession(
    val packageName: String,      // Which app was used
    val startTime: Long,          // When usage started (timestamp)
    val endTime: Long,            // When usage ended (timestamp)
    val durationMinutes: Int      // Total minutes used
)
```

**`GlobalSettings.kt`**
```kotlin
data class GlobalSettings(
    val globalDailyLimitMinutes: Int = 240,  // 4 hours default
    val bedtimeStart: String = "22:00",      // Bedtime start
    val bedtimeEnd: String = "07:00",        // Bedtime end
    val workStart: String = "09:00",         // Work start
    val workEnd: String = "17:00",           // Work end
    val emergencyContacts: List<String> = emptyList(),
    val adminPin: String = "",
    val isParentalControlEnabled: Boolean = false
)
```

#### `data/local/entity/` - Database Tables
**Purpose**: Define database table structures

**`AppLimitEntity.kt`**
```kotlin
@Entity(tableName = "app_limits")  // Room database table
data class AppLimitEntity(
    @PrimaryKey val packageName: String,  // Primary key
    val appName: String,
    val dailyLimitMinutes: Int,
    val category: String,  // Stored as string in database
    val isWhitelisted: Boolean,
    val isEducational: Boolean
)
```

**`UsageSessionEntity.kt`**
```kotlin
@Entity(tableName = "usage_sessions")
data class UsageSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,  // Auto-incrementing ID
    val packageName: String,
    val startTime: Long,
    val endTime: Long,
    val durationMinutes: Int,
    val date: String  // YYYY-MM-DD format
)
```

**`OverrideLogEntity.kt`**
```kotlin
@Entity(tableName = "override_logs")
data class OverrideLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,           // When override happened
    val packageName: String,       // Which app was overridden
    val reason: String,            // Why user overrode
    val durationMinutes: Int       // How long override lasted
)
```

#### `data/local/dao/` - Database Access Objects
**Purpose**: Define database queries and operations

**`AppLimitDao.kt`**
```kotlin
@Dao  // Room Data Access Object
interface AppLimitDao {
    @Query("SELECT * FROM app_limits")  // SQL query
    fun getAllAppLimits(): Flow<List<AppLimitEntity>>  // Returns live data

    @Query("SELECT * FROM app_limits WHERE packageName = :packageName")
    suspend fun getAppLimitByPackage(packageName: String): AppLimitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // Insert or update
    suspend fun insertAppLimit(appLimit: AppLimitEntity)
}
```

**`UsageSessionDao.kt`**
```kotlin
@Dao
interface UsageSessionDao {
    @Query("SELECT SUM(durationMinutes) FROM usage_sessions WHERE packageName = :packageName AND date = :date")
    suspend fun getTotalUsageByPackageAndDate(packageName: String, date: String): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageSession(usageSession: UsageSessionEntity)
}
```

**`OverrideLogDao.kt`**
```kotlin
@Dao
interface OverrideLogDao {
    @Query("SELECT * FROM override_logs ORDER BY timestamp DESC")
    fun getAllOverrideLogs(): Flow<List<OverrideLogEntity>>

    @Insert
    suspend fun insertOverrideLog(overrideLog: OverrideLogEntity)
}
```

#### `data/local/AppDatabase.kt`
**Purpose**: Main database class that ties everything together
```kotlin
@Database(
    entities = [AppLimitEntity::class, UsageSessionEntity::class, OverrideLogEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appLimitDao(): AppLimitDao
    abstract fun usageSessionDao(): UsageSessionDao
    abstract fun overrideLogDao(): OverrideLogDao

    companion object {
        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "screen_time_database"
            ).build()
        }
    }
}
```

### 📁 Services (`service/`)

Services run in the background and handle system-level operations.

#### `UsageTrackingService.kt`
**Purpose**: Monitors which apps are being used and for how long
```kotlin
@AndroidEntryPoint  // Enables Hilt injection
class UsageTrackingService : Service() {
    @Inject
    lateinit var usageSessionDao: UsageSessionDao

    private fun startUsageTracking() {
        serviceScope.launch {
            while (isActive) {
                trackCurrentAppUsage()  // Check which app is active
                delay(TRACKING_INTERVAL)  // Wait 5 seconds
            }
        }
    }

    private suspend fun trackCurrentAppUsage() {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        // Query Android system for app usage events
        // Store usage data in database
    }
}
```

**What it does:**
- Runs continuously in background
- Checks which app is currently active every 5 seconds
- Records usage sessions in database
- Triggers alerts when limits are reached

#### `OverlayService.kt`
**Purpose**: Shows blocking screens when apps are used too much
```kotlin
@AndroidEntryPoint
class OverlayService : Service() {
    private fun showBlockingOverlay(packageName: String, appName: String, remainingTime: Int) {
        val layoutParams = WindowManager.LayoutParams().apply {
            type = OVERLAY_TYPE  // Show over other apps
            flags = FLAG_NOT_FOCUSABLE or FLAG_NOT_TOUCH_MODAL
            width = MATCH_PARENT
            height = MATCH_PARENT
        }

        // Create blocking screen view
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_blocking_screen, null)
        windowManager.addView(overlayView, layoutParams)
    }
}
```

**What it does:**
- Shows full-screen overlay when app limit reached
- Prevents user from using restricted apps
- Provides emergency override option
- Logs all override attempts

### 📁 UI Layer (`ui/`)

The UI layer handles all user interface components using Jetpack Compose.

#### `MainActivity.kt`
**Purpose**: Main entry point for the app's user interface
```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenTimeControlTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenTimeApp()  // Main app content
                }
            }
        }
    }
}
```

#### `ScreenTimeApp.kt`
**Purpose**: Main app composable with navigation
```kotlin
@Composable
fun ScreenTimeApp() {
    val navController = rememberNavController()  // Navigation controller

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { navController.navigate("dashboard") },
                    label = { Text("Dashboard") }
                )
                // More navigation items...
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "dashboard"
        ) {
            composable("dashboard") { DashboardScreen() }
            composable("limits") { LimitsScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}
```

#### `ui/screens/` - Individual Screens

**`DashboardScreen.kt`**
**Purpose**: Shows usage summary and progress
```kotlin
@Composable
fun DashboardScreen() {
    var totalUsage by remember { mutableStateOf(0) }
    var globalLimit by remember { mutableStateOf(240) }

    Column {
        // Header
        Text("Screen Time Dashboard")

        // Usage Card
        Card {
            LinearProgressIndicator(
                progress = (totalUsage.toFloat() / globalLimit.toFloat())
            )
            Text("$totalUsage minutes used of $globalLimit minutes")
        }

        // App Usage List
        LazyColumn {
            items(usageList) { usage ->
                AppUsageCard(usage = usage)
            }
        }
    }
}
```

**`LimitsScreen.kt`**
**Purpose**: Manage app-specific time limits
```kotlin
@Composable
fun LimitsScreen() {
    var appLimits by remember { mutableStateOf<List<AppLimit>>(emptyList()) }

    Column {
        // Category Filter
        LazyRow {
            items(AppCategory.values()) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category.displayName) }
                )
            }
        }

        // App Limits List
        LazyColumn {
            items(appLimits) { appLimit ->
                AppLimitCard(appLimit = appLimit)
            }
        }
    }
}
```

**`SettingsScreen.kt`**
**Purpose**: Configure global settings and permissions
```kotlin
@Composable
fun SettingsScreen() {
    var globalLimit by remember { mutableStateOf("240") }
    var bedtimeStart by remember { mutableStateOf("22:00") }

    Column {
        // Global Settings Card
        Card {
            OutlinedTextField(
                value = globalLimit,
                onValueChange = { globalLimit = it },
                label = { Text("Global Daily Limit (minutes)") }
            )
        }

        // Security Settings
        Card {
            OutlinedTextField(
                value = adminPin,
                onValueChange = { adminPin = it },
                label = { Text("Admin PIN") }
            )
        }

        // Permissions Section
        Card {
            PermissionItem(
                title = "Usage Access",
                description = "Required to track app usage",
                isGranted = true
            )
        }
    }
}
```

### 📁 Business Logic (`manager/`)

#### `UsageManager.kt`
**Purpose**: Core business logic for usage tracking and limit enforcement
```kotlin
@Singleton
class UsageManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val usageSessionDao: UsageSessionDao,
    private val overrideLogDao: OverrideLogDao
) {
    fun checkAndEnforceLimits(packageName: String, appName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
            val totalUsage = usageSessionDao.getTotalUsageByPackageAndDate(packageName, today) ?: 0
            val appLimit = 60  // Get from database

            if (totalUsage >= appLimit) {
                showBlockingOverlay(packageName, appName, 0)
            }
        }
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
}
```

### 📁 Dependency Injection (`di/`)

#### `DatabaseModule.kt`
**Purpose**: Provides database dependencies to other components
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideAppLimitDao(database: AppDatabase): AppLimitDao {
        return database.appLimitDao()
    }

    @Provides
    fun provideUsageSessionDao(database: AppDatabase): UsageSessionDao {
        return database.usageSessionDao()
    }
}
```

## 🔄 Data Flow

### 1. App Launch Flow
```
ScreenTimeApplication.kt → MainActivity.kt → ScreenTimeApp.kt → DashboardScreen.kt
```

### 2. Usage Tracking Flow
```
UsageTrackingService.kt → UsageStatsManager → UsageSessionDao → AppDatabase
```

### 3. Limit Enforcement Flow
```
UsageManager.kt → checkAndEnforceLimits() → OverlayService.kt → Blocking Screen
```

### 4. Settings Flow
```
SettingsScreen.kt → User Input → GlobalSettings → Database
```

## 🛠️ Key Kotlin Concepts Used

### 1. Data Classes
```kotlin
data class AppLimit(
    val packageName: String,
    val dailyLimitMinutes: Int
)
```
- Automatically generates getters, setters, equals(), hashCode()
- Used for simple data containers

### 2. Coroutines
```kotlin
suspend fun someFunction() {
    delay(1000)  // Non-blocking delay
    // Do work
}
```
- Asynchronous programming without callbacks
- `suspend` functions can be paused and resumed

### 3. Flow
```kotlin
fun getAllAppLimits(): Flow<List<AppLimitEntity>>
```
- Reactive streams that emit values over time
- Automatically updates UI when data changes

### 4. Dependency Injection
```kotlin
@Inject
lateinit var usageSessionDao: UsageSessionDao
```
- Automatically provides dependencies
- Reduces coupling between components

### 5. Composable Functions
```kotlin
@Composable
fun MyScreen() {
    Text("Hello World")
}
```
- Declarative UI functions
- Automatically recompose when state changes

## 🔧 Common Issues and Solutions

### 1. Gradle Sync Failed
**Solution**:
- Check internet connection
- File → Invalidate Caches and Restart
- Update Android Studio

### 2. Permission Denied
**Solution**:
- Go to Settings → Apps → Screen Time Control → Permissions
- Grant all required permissions

### 3. App Crashes on Launch
**Solution**:
- Check Logcat in Android Studio for error messages
- Ensure all dependencies are properly synced

### 4. Database Errors
**Solution**:
- Uninstall and reinstall app (clears database)
- Check database migrations

## 📝 Next Steps

1. **Understand the Basics**: Read through each file and understand its purpose
2. **Run the App**: Build and run on a device to see it in action
3. **Modify Settings**: Try changing limits and see how it affects the app
4. **Add Features**: Start with small modifications like changing colors or text
5. **Study Kotlin**: Learn more Kotlin concepts as you work with the code

## 🎯 Key Takeaways

- **Separation of Concerns**: Each file has a specific purpose
- **Reactive Programming**: UI updates automatically when data changes
- **Background Processing**: Services run independently of UI
- **Database First**: All data is stored locally in SQLite
- **Modern Android**: Uses latest Android development practices

This codebase demonstrates modern Android development with Kotlin, Jetpack Compose, Room database, and clean architecture principles. Each component works together to create a comprehensive screen time management application.