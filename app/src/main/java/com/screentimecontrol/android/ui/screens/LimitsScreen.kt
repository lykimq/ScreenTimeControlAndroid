package com.screentimecontrol.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.screentimecontrol.android.data.model.AppLimit
import com.screentimecontrol.android.data.model.AppCategory

@Composable
fun LimitsScreen() {
    var appLimits by remember { mutableStateOf<List<AppLimit>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf(AppCategory.SOCIAL_MEDIA) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "App Limits",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Category Filter
        Text(
            text = "Filter by Category",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
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
            items(appLimits.filter { it.category == selectedCategory }) { appLimit ->
                AppLimitCard(appLimit = appLimit)
            }
        }

        // Add New Limit Button
        Button(
            onClick = { /* TODO: Show add limit dialog */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Add New Limit")
        }
    }
}

@Composable
fun AppLimitCard(appLimit: AppLimit) {
    var limitMinutes by remember { mutableStateOf(appLimit.dailyLimitMinutes.toString()) }
    var isWhitelisted by remember { mutableStateOf(appLimit.isWhitelisted) }
    var isEducational by remember { mutableStateOf(appLimit.isEducational) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = appLimit.appName,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = appLimit.packageName,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = appLimit.category.displayName,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Limit Input
            OutlinedTextField(
                value = limitMinutes,
                onValueChange = { limitMinutes = it },
                label = { Text("Daily Limit (minutes)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Toggle Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isWhitelisted,
                        onCheckedChange = { isWhitelisted = it }
                    )
                    Text("Whitelisted")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isEducational,
                        onCheckedChange = { isEducational = it }
                    )
                    Text("Educational")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Delete limit */ }
                ) {
                    Text("Delete")
                }

                Button(
                    onClick = { /* TODO: Save changes */ }
                ) {
                    Text("Save")
                }
            }
        }
    }
}