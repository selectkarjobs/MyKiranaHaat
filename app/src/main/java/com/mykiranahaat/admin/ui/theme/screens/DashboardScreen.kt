package com.mykiranahaat.admin.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Menu items as before, Dashboard at center
    val bottomNavItems = listOf(
        NavItem.Stores,
        NavItem.Products,
        NavItem.Dashboard, // Center
        NavItem.Customers,
        NavItem.Drivers
    )

    val purple = Color(0xFF8254CB)
    val surfacePurple = Color(0xFFE1D5F5)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { Box(Modifier.fillMaxSize()) }
    ) {
        Box(Modifier.fillMaxSize().background(surfacePurple)) {
            Scaffold(
                containerColor = Color.Transparent,
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
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(88.dp)
                            .background(Color.Transparent)
                    ) {
                        // Draw the custom cutout using Canvas
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(78.dp)
                                .align(Alignment.BottomCenter)
                                .zIndex(1f)
                        ) {
                            val barHeight = size.height
                            val barWidth = size.width
                            val cutoutRadius = 34.dp.toPx()
                            val cutoutCenter = Offset(barWidth / 2, 0f)
                            val barPath = Path().apply {
                                // Top left corner
                                moveTo(0f, 0f)
                                arcTo(
                                    rect = Rect(
                                        left = 0f,
                                        top = 0f,
                                        right = 64.dp.toPx(),
                                        bottom = 64.dp.toPx()
                                    ),
                                    startAngleDegrees = 180f,
                                    sweepAngleDegrees = 90f,
                                    forceMoveTo = false
                                )
                                lineTo(cutoutCenter.x - cutoutRadius - 12.dp.toPx(), 0f)
                                cubicTo(
                                    cutoutCenter.x - cutoutRadius, 0f,
                                    cutoutCenter.x - cutoutRadius / 2, cutoutRadius,
                                    cutoutCenter.x, cutoutRadius + 6.dp.toPx()
                                )
                                cubicTo(
                                    cutoutCenter.x + cutoutRadius / 2, cutoutRadius,
                                    cutoutCenter.x + cutoutRadius, 0f,
                                    cutoutCenter.x + cutoutRadius + 12.dp.toPx(), 0f
                                )
                                lineTo(barWidth - 64.dp.toPx(), 0f)
                                arcTo(
                                    rect = Rect(
                                        left = barWidth - 64.dp.toPx(),
                                        top = 0f,
                                        right = barWidth,
                                        bottom = 64.dp.toPx()
                                    ),
                                    startAngleDegrees = 270f,
                                    sweepAngleDegrees = 90f,
                                    forceMoveTo = false
                                )
                                lineTo(barWidth, barHeight)
                                lineTo(0f, barHeight)
                                lineTo(0f, 0f)
                                close()
                            }
                            drawIntoCanvas {
                                it.drawPath(
                                    barPath,
                                    Paint().apply {
                                        color = purple
                                        isAntiAlias = true
                                    }
                                )
                            }
                        }

                        // Row for nav items and a Spacer for center button
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(78.dp)
                                .align(Alignment.BottomCenter)
                                .zIndex(2f),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            bottomNavItems.forEach { item ->
                                val isDashboard = item == NavItem.Dashboard
                                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                                val selected = currentRoute == item.route
                                val iconColor = if (selected) Color.White else Color.White.copy(alpha = 0.7f)
                                val labelColor = if (selected) Color.White else Color.White.copy(alpha = 0.7f)

                                if (isDashboard) {
                                    // Add enough space for the center button so it doesn't overlap
                                    Spacer(modifier = Modifier.width(68.dp))
                                } else {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .weight(1f, fill = false)
                                            .clickable {
                                                if (currentRoute != item.route) navController.navigate(item.route) {
                                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                    ) {
                                        Icon(
                                            item.icon,
                                            contentDescription = item.label,
                                            modifier = Modifier.size(28.dp),
                                            tint = iconColor
                                        )
                                        Text(
                                            item.label,
                                            fontSize = 13.sp,
                                            color = labelColor
                                        )
                                    }
                                }
                            }
                        }

                        // Center Floating Button (Dashboard) fills cutout
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = 8.dp)
                                .size(60.dp) // Slightly smaller for less overlap
                                .zIndex(10f)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable {
                                    if (currentRoute != NavItem.Dashboard.route) navController.navigate(NavItem.Dashboard.route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = NavItem.Dashboard.icon,
                                contentDescription = NavItem.Dashboard.label,
                                modifier = Modifier.size(32.dp),
                                tint = purple
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
}

sealed class NavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Dashboard : NavItem("dashboard", "Dashboard", Icons.Default.Home)
    object Stores : NavItem("stores", "Stores", Icons.Default.ShoppingCart)
    object Products : NavItem("products", "Products", Icons.Default.ShoppingCart)
    object Customers : NavItem("customers", "Customers", Icons.Default.Person)
    object Drivers : NavItem("drivers", "Drivers", Icons.Default.Info)
}

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