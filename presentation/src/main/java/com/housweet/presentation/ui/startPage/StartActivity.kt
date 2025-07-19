package com.housweet.presentation.ui.startPage

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.housweet.domain.event.AuthEvent
import com.housweet.domain.event.AuthEventBus
import com.housweet.presentation.ui.theme.HousweetTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : ComponentActivity() {
    @Inject
    lateinit var authEventBus: AuthEventBus
    private val startViewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isFailedRefreshToken = intent.getBooleanExtra("isFailedRefreshToken", false)

        lifecycleScope.launch {
            authEventBus.events.collect { event ->
                when (event) {
                    is AuthEvent.TokenRefreshFailed -> {
                        startViewModel.logout {
                            handleTokenRefreshFailure()
                        }
                    }
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            HousweetTheme {
                Start(isFailedRefreshToken = isFailedRefreshToken)
            }
        }
    }

    private fun handleTokenRefreshFailure() {
        val intent = Intent(this, StartActivity::class.java)
        intent.putExtra("isFailedRefreshToken", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

@Composable
fun Start(isFailedRefreshToken: Boolean) {
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        if (isFailedRefreshToken) {
            snackBarHostState.showSnackbar(
                message = "토큰 갱신에 실패했습니다. 다시 로그인해주세요.",
                actionLabel = "닫기",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        StartPageNavigation(
            modifier = Modifier.padding(paddingValues),
            isFailedRefreshToken = isFailedRefreshToken
        )
    }
}