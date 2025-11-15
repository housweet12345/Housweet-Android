package com.housweet.presentation.ui.debug

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.housweet.presentation.BuildConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DebugConfigActivity : ComponentActivity() {
    private val viewModel: DebugConfigViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!BuildConfig.IS_DEBUG) {
            finish()
            return
        }

        setContent {
            MaterialTheme {
                DebugConfigScreen(
                    viewModel = viewModel,
                    onFinish = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugConfigScreen(
    viewModel: DebugConfigViewModel,
    onFinish: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var baseUrl by remember { mutableStateOf("") }
    var userBaseUrl by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        baseUrl = uiState.currentBaseUrl
        userBaseUrl = uiState.currentUserBaseUrl
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text("Debug Configuration") },
            navigationIcon = {
                TextButton(onClick = onFinish) {
                    Text("Back")
                }
            }
        )

        if (!uiState.isDebugMode) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    "Debug mode is not available in release builds",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            return@Column
        }

        Text(
            "Current Configuration",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Base URL: ${uiState.currentBaseUrl}")
                Text("User Base URL: ${uiState.currentUserBaseUrl}")
            }
        }

        Text(
            "Override URLs",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = baseUrl,
            onValueChange = { baseUrl = it },
            label = { Text("Base URL") },
            placeholder = { Text("https://api.example.com") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
        )

        OutlinedTextField(
            value = userBaseUrl,
            onValueChange = { userBaseUrl = it },
            label = { Text("User Base URL") },
            placeholder = { Text("https://user-api.example.com") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    viewModel.setBaseUrl(baseUrl)
                    viewModel.setUserBaseUrl(userBaseUrl)
                },
                modifier = Modifier.weight(1f),
                enabled = uiState.isDebugMode
            ) {
                Text("Apply")
            }

            OutlinedButton(
                onClick = {
                    viewModel.clearDebugUrls()
                    baseUrl = ""
                    userBaseUrl = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }
        }

        if (uiState.message.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.isSuccess) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Text(
                    uiState.message,
                    modifier = Modifier.padding(16.dp),
                    color = if (uiState.isSuccess) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    }
                )
            }
        }
    }
}