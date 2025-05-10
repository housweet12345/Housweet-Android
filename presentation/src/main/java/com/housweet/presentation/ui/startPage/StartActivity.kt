package com.housweet.presentation.ui.startPage

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.housweet.presentation.ui.startPage.navigation.StartPageNavigation
import com.housweet.presentation.ui.theme.HousweetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HousweetTheme {
                Start()
            }
        }
    }
}

@Composable
fun Start() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        StartPageNavigation(modifier = Modifier.padding(paddingValues))
    }
}