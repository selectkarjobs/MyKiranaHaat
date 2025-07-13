package com.mykiranahaat.admin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Info // fallback icon for Drivers
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomNavItems = listOf(
        NavItem.Dashboard,
        NavItem.Stores,
        NavItem.Products,
        NavItem.Customers,
        NavItem.Drivers
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Blank drawer for now
            Box(Modifier.fillMaxSize())
        }
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(
                            bottomNavItems.firstOrNull { it.route == navController.currentBackStackEntryAsState().value?.destination?.route }?.label
                                ?: "Dashboard"
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (drawerState.isClosed) scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Home, contentDescription = "Open drawer")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavItem.Dashboard.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(NavItem.Dashboard.route) { DashboardHome() }
                composable(NavItem.Stores.route) { StoresScreen() }
                composable(NavItem.Products.route) { ProductsScreen() }
                composable(NavItem.Customers.route) { CustomersScreen() }
                composable(NavItem.Drivers.route) { DriversScreen() }
            }
        }
    }
}

sealed class NavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Dashboard : NavItem("dashboard", "Dashboard", Icons.Default.Home)
    object Stores : NavItem("stores", "Stores", Icons.Default.ShoppingCart) // Substitute icon for store
    object Products : NavItem("products", "Products", Icons.Default.ShoppingCart)
    object Customers : NavItem("customers", "Customers", Icons.Default.Person) // Use Person icon for customers
    object Drivers : NavItem("drivers", "Drivers", Icons.Default.Info) // Fallback icon for drivers
}

// Blank content for all activities (screens)
@Composable
fun DashboardHome() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Dashboard Content")
    }
}

@Composable
fun StoresScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Stores Content")
    }
}

@Composable
fun ProductsScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Products Content")
    }
}

@Composable
fun CustomersScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Customers Content")
    }
}

@Composable
fun DriversScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Drivers Content")
    }
}