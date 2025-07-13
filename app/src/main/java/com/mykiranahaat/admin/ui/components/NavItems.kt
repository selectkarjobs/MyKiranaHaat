package com.mykiranahaat.admin.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class CurvedNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val curvedNavItems = listOf(
    CurvedNavItem("stores", "Stores", Icons.Filled.ShoppingCart),
    CurvedNavItem("products", "Products", Icons.Filled.List),
    CurvedNavItem("dashboard", "Dashboard", Icons.Filled.Home),
    CurvedNavItem("drivers", "Drivers", Icons.Filled.Person),
    CurvedNavItem("customers", "Customers", Icons.Filled.Person)
)