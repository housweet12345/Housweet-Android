package com.housweet.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.presentation.R
import com.housweet.presentation.ui.startPage.StartActivity
import com.housweet.presentation.ui.theme.HousweetTheme
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HousweetTheme {
                SplashScreen()
            }
        }
    }
}

@Composable
fun SplashScreen(splashViewModel: SplashViewModel = hiltViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        splashViewModel.isLogin.collect { isAutoLogin ->
            Intent(context, StartActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("isLogin", isAutoLogin)
            }.let { intent ->
                context.startActivity(intent)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.bang),
            contentDescription = "Splash Image",
            modifier = Modifier
                .size(100.dp),
            tint = Purple
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}