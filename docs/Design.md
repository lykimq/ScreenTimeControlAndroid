# Screen Time Control for Android

## Core Features

Main goal should remain: calling, texting, navigation, emergencies - not mindless scrolling.

### 1. Customizable Time Budgets
- Per-app time limits: e.g., 30 mins/day for IG, 1 hour/day for YouTube, etc.
- Category budgets: e.g., 2 hours/day total for Social Media.
- Global screen time limit: e.g., 4 hours/day for the entire device (except whitelisted apps).

### 2. Smart Lockdown Modes
When the user hits their limit:
- Whitelist essentials: Only allow calls, messages, contacts, camera, maps and emergency settings.
- Custom inactive apps: User can predefine which apps must lock when over the limit.
- Force break mode: Lock non-essential apps for a custom time (e.g., "No social for next 2 hours").

### 3. Scheduled Downtime
- Block distracting apps during bedtime or work hours.
- Allow exceptions for urgent calls for family contacts.

### 4. Gentle Nudges
- Smart reminders: "You have spent 80% of your time budget for IG today."
- Weekly insights: Highlight worst time sinks and suggest adjustments.

### 5. Anti-bypass Measures
- PIN code or biometric unlock for override.
- Strong admin mode for parental control.
- Prevent easy uninstall without passcode.

### 6. Smart Allowlists
- Certain apps (phone, maps, contacts, alarms) are always allowed.
- Option to allow educational apps even after time runs out.

### 7. Emergency Mode
- A way to override for urgent needs, with reason logging so the user sees when/why they overrode the limit.


## Technial Architecture

On Android, it is possible to create quite a powerful screen time enforcer app, because Android:
- Allows you to track app usage (via `UsageStatsManager`).
- Lets you draw overlays, show lock screens, or block apps by launching blocking activities.
- Has special APIs for device administration and parental control.

### Language
- Kotlin (modern)

### Main APIs/tech:
- `UsageStatsManager` - track app usage.
- `DevicePolicyManager` - restrict features.
- `AccessibilityService` - detect foreground app, sometimes block it.
- `SYSTEM_ALERT_WINDOW` or `draw over lays` - show your custom lock screen when time is up.
- Scheduled jobs or alarms to check daily limits.

## First Version
1. Android
2. Prototype:
- Daily limits per app.
- Global limit.
- Blocking screen/overlay.
3. Add a backend (Node.js, Firebase, etc) if we want sync or accountability.

### Architecture Diagram
Below is the Android screen time controller at high level:

+----------------------------------------+
| Android App (Kotlin)                   |
+----------------------------------------+
|                                        |
|  UI Layer (Jetpack Compose)            |
| - Usage summary screen                 |
| - Limits setup screen                  |
| - Locked overlay screen                |
| - Settings screen                      |
+----------------------------------------+
|                                        |
|  Business Logic Layer                  |
| - Daily per-app limit logic            |
| - Global screen time logic             |
| - Scheduler/timers                     |
| - Usage tracking service               |
| - Override management                  |
+----------------------------------------+
|                                        |
|  System Control Layer                  |
| - UsageStatsManager                    |
| - AccessibilityService                 |
| - Overlay/lock screen                  |
| - Permissions management               |
| - Background job scheduling            |
+----------------------------------------+
|  Local Data Layer                      |
| - Room Database                        |
| - SharedPreferences                    |
| - Stores limits, usage, settings       |
+----------------------------------------+
|                                        |
| (Optional) Backend                     |
| - Firebase Auth (user login)           |
| - Firestore or Realtime DB             |
| - Sync usage, limits, reports          |
+----------------------------------------+

Key idea:
- All time-tracking and blocking logic is **on-device**.
- The backend (Firebase or Node.js) is optional - only needed for cross-device sync or accountability.
- The overlay is shown when the user reaches their limit.

### Key Implementation Details

#### 1. Usage Tracking Service
- Background service that monitors app usage
- Uses `UsageStatsManager` to get app usage data
- Stores sessions in Room database
- Triggers alerts when limits are reached

#### 2. Overlay Service
- Shows blocking overlay when limits are exceeded
- Prevents access to restricted apps
- Provides emergency override option
- Logs all override attempts

#### 3. Accessibility Service
- Monitor foreground app changes
- Can block apps by showing overlay
- Handles system-level app switching

#### 4. Background Jobs
- Daily usage reset at midnight
- Periodic usage checks
- Scheduled downtime enforcement
- Weekly report generation

#### 5. Security Features
- Biometric authentication for overrides
- Admin PIN for parental controls
- Tamper detection and alerts
- Secure storage of sensitive data

### Error Handling Strategy
1. **Permission Denied**: Graceful fallback with user guidance
2. **Service Killed**: Auto-restart with WorkManager
3. **Database Corruption**: Backup and restore mechanism
4. **System Updates**: Version compability checks
5. **Battery Optimization**: Guide users to disable for app

### Testing Strategy
1. **Unit Tests**: Business logic, data models, utilities
2. **Integration Tests**: Database operations, service interactions
3. **UI Tests**: Compose UI components and flows
4. **System Tests**: End-to-end usage scenarios
5. **Performance Tests**: Memory usage, battery impact

## Project Structure

```
ScreenTimeControlAndroid/
|-- app/
|   |-- build.gradle.kts   # Dependencies and configuration
|   |-- src/main/
|   |     |-- AndroidManifest.xml  # Permissions and services
|   |     |-- java/com/screentimecontrol/android
|   |     |     |-- data/     # Room database and models
|   |     |     |-- service/  # Background services
|   |     |     |-- ui/       # Jetpack Compose UI
|   |     |     |-- manager/  # Business logic
|   |     |     |-- di/       # Dependency injection
|   |     |-- res/                # Resources and layouts
|-- build.gradle.kts       # Project configuration
|-- setting.gradle.kts     # Project settings
|-- docs/Design.md         # Design document
```
