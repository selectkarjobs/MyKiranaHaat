package com.mykiranahaat.admin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // Added import
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF6200EE), Color(0xFF3700B3))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(MaterialTheme.shapes.medium),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to MyKiranaHaat Admin",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EE)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Manage your Kirana store here.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* TODO: Add dashboard actions */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE),
                        contentColor = Color.White
                    )
                ) {
                    Text("View Orders")
                }
            }
        }
    }
}