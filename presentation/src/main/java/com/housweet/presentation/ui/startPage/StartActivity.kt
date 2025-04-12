package com.housweet.presentation.ui.startPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.housweet.presentation.ui.startPage.loginPage.loginScreen.LoginScreen
import com.housweet.presentation.ui.theme.HousweetTheme

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HousweetTheme {
                LoginScreen()
            }
        }
    }
}