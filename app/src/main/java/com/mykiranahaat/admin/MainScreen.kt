package com.mykiranahaat.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mykiranahaat.admin.ui.components.DynamicCurvedBottomNavBar
import com.mykiranahaat.admin.ui.components.curvedNavItems
import com.mykiranahaat.admin.ui.screens.CustomersScreen
import com.mykiranahaat.admin.ui.screens.DashboardScreen
import com.mykiranahaat.admin.ui.screens.DriversScreen
import com.mykiranahaat.admin.ui.screens.ProductsScreen
import com.mykiranahaat.admin.ui.screens.StoresScreen

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(2) } // Default to Dashboard

    Scaffold(
        bottomBar = {
            DynamicCurvedBottomNavBar(
                selectedIndex = selectedTab,
                onItemSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (curvedNavItems[selectedTab].route) {
                "dashboard" -> DashboardScreen()
                "stores" -> StoresScreen()
                "products" -> ProductsScreen()
                "drivers" -> DriversScreen()
                "customers" -> CustomersScreen()
            }
        }
    }
}