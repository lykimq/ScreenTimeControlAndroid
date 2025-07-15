package com.screentimecontrol.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.screentimecontrol.android.data.model.GlobalSettings

@Composable
fun SettingsScreen() {
    var globalLimit by remember { mutableStateOf("240") }
    var bedtimeStart by remember { mutableStateOf("22:00") }
    var bedtimeEnd by remember { mutableStateOf("07:00") }
    var workStart by remember { mutableStateOf("09:00") }
    var workEnd by remember { mutableStateOf("17:00") }
    var adminPin by remember { mutableStateOf("") }
    var isParentalControlEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Global Settings
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Global Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = globalLimit,
                    onValueChange = { globalLimit = it },
                    label = { Text("Global Daily Limit (minutes)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bedtime Schedule",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = bedtimeStart,
                        onValueChange = { bedtimeStart = it },
                        label = { Text("Start") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = bedtimeEnd,
                        onValueChange = { bedtimeEnd = it },
                        label = { Text("End") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Work Schedule",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = workStart,
                        onValueChange = { workStart = it },
                        label = { Text("Start") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = workEnd,
                        onValueChange = { workEnd = it },
                        label = { Text("End") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Security Settings
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Security",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = adminPin,
                    onValueChange = { adminPin = it },
                    label = { Text("Admin PIN") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isParentalControlEnabled,
                        onCheckedChange = { isParentalControlEnabled = it }
                    )
                    Text("Enable Parental Controls")
                }
            }
        }

        // Permissions Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Permissions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                PermissionItem(
                    title = "Usage Access",
                    description = "Required to track app usage",
                    isGranted = true // TODO: Check actual permission
                )

                PermissionItem(
                    title = "Overlay Permission",
                    description = "Required to show blocking screens",
                    isGranted = false // TODO: Check actual permission
                )

                PermissionItem(
                    title = "Accessibility Service",
                    description = "Required to monitor app switching",
                    isGranted = false // TODO: Check actual permission
                )
            }
        }

        // Save Button
        Button(
            onClick = { /* TODO: Save settings */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Settings")
        }
    }
}

@Composable
fun PermissionItem(
    title: String,
    description: String,
    isGranted: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (isGranted) {
            Text(
                text = "Granted",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp
            )
        } else {
            OutlinedButton(
                onClick = { /* TODO: Request permission */ }
            ) {
                Text("Grant")
            }
        }
    }
}