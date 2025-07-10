package com.mykiranahaat.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mykiranahaat.admin.ui.screens.DashboardScreen
import com.mykiranahaat.admin.ui.screens.LoginScreen
import com.mykiranahaat.admin.ui.screens.SignupScreen
import com.mykiranahaat.admin.ui.theme.MyKiranaHaatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyKiranaHaatTheme {
                MyKiranaHaatApp()
            }
        }
    }
}

@Composable
fun MyKiranaHaatApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToSignup = { navController.navigate("signup") },
                onLoginSuccess = { navController.navigate("dashboard") { popUpTo("login") { inclusive = true } } }
            )
        }
        composable("signup") {
            SignupScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onSignupSuccess = { navController.navigate("dashboard") { popUpTo("signup") { inclusive = true } } }
            )
        }
        composable("dashboard") {
            DashboardScreen()
        }
    }
}