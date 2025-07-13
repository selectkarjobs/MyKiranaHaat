package com.mykiranahaat.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.mykiranahaat.admin.ui.theme.MyKiranaHaatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyKiranaHaatTheme {
                MainScreen()
            }
        }
    }
}