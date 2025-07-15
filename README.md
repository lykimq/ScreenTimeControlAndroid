# Screen Time Control for Android

A comprehensive Android application designed to help users manage their screen time and digital wellbeing. The app provides customizable time budgets, smart lockdown modes, and gentle nudges to promote healthier device usage habits.

## 🎯 Core Features

### 1. Customizable Time Budgets
- **Per-app time limits**: Set daily limits for individual apps (e.g., 30 mins/day for Instagram, 1 hour/day for YouTube)
- **Category budgets**: Set limits for app categories (e.g., 2 hours/day total for Social Media)
- **Global screen time limit**: Set overall device usage limits (e.g., 4 hours/day)

### 2. Smart Lockdown Modes
- **Whitelist essentials**: Always allow calls, messages, contacts, camera, maps, and emergency settings
- **Custom app blocking**: Predefine which apps must lock when limits are exceeded
- **Force break mode**: Lock non-essential apps for custom durations

### 3. Scheduled Downtime
- Block distracting apps during bedtime or work hours
- Allow exceptions for urgent calls from family contacts

### 4. Gentle Nudges
- Smart reminders when approaching limits (e.g., "80% of Instagram time used")
- Weekly insights highlighting time sinks and suggesting adjustments

### 5. Anti-bypass Measures
- PIN code or biometric authentication for overrides
- Strong admin mode for parental controls
- Prevent easy uninstallation without passcode

### 6. Smart Allowlists
- Essential apps (phone, maps, contacts, alarms) always allowed
- Educational apps can remain accessible even after time limits

### 7. Emergency Mode
- Override capability for urgent needs with reason logging
- Transparent tracking of when/why limits were bypassed

## 🏗️ Technical Architecture

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Database**: Room with SQLite
- **Dependency Injection**: Hilt
- **Background Processing**: WorkManager
- **Navigation**: Navigation Compose

### Key Android APIs
- `UsageStatsManager` - Track app usage
- `DevicePolicyManager` - Restrict device features
- `AccessibilityService` - Monitor foreground apps
- `SYSTEM_ALERT_WINDOW` - Show blocking overlays
- `BiometricManager` - Secure authentication

### Project Structure
```
app/src/main/java/com/screentimecontrol/android/
├── data/
│   ├── local/
│   │   ├── dao/           # Room DAOs
│   │   ├── entity/        # Database entities
│   │   └── AppDatabase.kt
│   └── model/             # Data models
├── service/
│   ├── UsageTrackingService.kt
│   ├── OverlayService.kt
│   └── ScreenTimeAccessibilityService.kt
├── ui/
│   ├── screens/           # Compose screens
│   └── theme/            # UI theme
└── ScreenTimeApplication.kt
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 26+ (API level 26)
- Kotlin 1.9.0+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/ScreenTimeControlAndroid.git
   cd ScreenTimeControlAndroid
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory and select it

3. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" (green play button) in Android Studio
   - The app will install and launch on your device

### Required Permissions

The app requires several permissions to function properly:

- **Usage Access**: To track app usage statistics
- **Overlay Permission**: To show blocking screens
- **Accessibility Service**: To monitor app switching
- **Biometric**: For secure authentication
- **Foreground Service**: For background usage tracking

Users will be prompted to grant these permissions during first launch.

## 📱 Usage Guide

### Initial Setup
1. Launch the app
2. Grant required permissions when prompted
3. Set up your global daily limit
4. Add apps and set individual limits
5. Configure bedtime and work schedules

### Dashboard
- View daily usage summary
- See progress towards limits
- Monitor app-specific usage

### Limits Management
- Add/remove app limits
- Set category-based limits
- Configure whitelist and educational apps

### Settings
- Adjust global settings
- Configure security options
- Manage permissions

## 🔧 Development

### Building from Source

1. **Setup Development Environment**
   ```bash
   # Ensure you have the latest Android SDK
   sdkmanager --update

   # Install required build tools
   sdkmanager "build-tools;34.0.0"
   ```

2. **Build the Project**
   ```bash
   ./gradlew build
   ```

3. **Run Tests**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

### Key Components

#### Usage Tracking Service
- Background service monitoring app usage
- Stores session data in Room database
- Triggers alerts when limits are reached

#### Overlay Service
- Shows blocking screens when limits exceeded
- Provides emergency override functionality
- Logs all override attempts

#### Accessibility Service
- Monitors foreground app changes
- Can block apps by showing overlays
- Handles system-level app switching

### Database Schema

The app uses Room database with three main entities:

- **AppLimitEntity**: Stores app-specific limits and settings
- **UsageSessionEntity**: Tracks individual usage sessions
- **OverrideLogEntity**: Logs override attempts and reasons

## 🧪 Testing

### Unit Tests
- Business logic testing
- Data model validation
- Repository layer testing

### Integration Tests
- Database operations
- Service interactions
- Permission handling

### UI Tests
- Compose component testing
- Navigation flow testing
- User interaction scenarios

## 🔒 Security Features

- **Biometric Authentication**: Secure override access
- **Admin PIN**: Parental control protection
- **Tamper Detection**: Monitor for bypass attempts
- **Secure Storage**: Encrypted sensitive data
- **Audit Logging**: Track all override attempts

## 📊 Performance Considerations

- **Battery Optimization**: Efficient background processing
- **Memory Management**: Proper lifecycle handling
- **Database Optimization**: Indexed queries and efficient storage
- **UI Responsiveness**: Smooth Compose animations

## 📞 Support

**Note**: This app is designed to promote digital wellbeing and should be used responsibly. The goal is to help users maintain healthy device usage habits while ensuring essential functionality remains accessible.

