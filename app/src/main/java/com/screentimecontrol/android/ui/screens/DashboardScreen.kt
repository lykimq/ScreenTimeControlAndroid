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
import com.screentimecontrol.android.data.model.DailyUsage
import com.screentimecontrol.android.data.model.AppLimit

@Composable
fun DashboardScreen() {
    var totalUsage by remember { mutableStateOf(0) }
    var globalLimit by remember { mutableStateOf(240) } // 4 hours default
    var usageList by remember { mutableStateOf<List<DailyUsage>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Screen Time Dashboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Global Usage Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Today's Usage",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = (totalUsage.toFloat() / globalLimit.toFloat()).coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$totalUsage minutes used of $globalLimit minutes",
                    fontSize = 14.sp
                )
            }
        }

        // App Usage List
        Text(
            text = "App Usage",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(usageList) { usage ->
                AppUsageCard(usage = usage)
            }
        }
    }
}

@Composable
fun AppUsageCard(usage: DailyUsage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = usage.packageName,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${usage.totalMinutes} minutes",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // TODO: Add progress indicator for individual app limits
        }
    }
}