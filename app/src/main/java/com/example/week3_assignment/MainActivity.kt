package com.example.week3_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.week3_assignment.ui.HomeScreen
import com.example.week3_assignment.ui.HomeViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.attributes = window.attributes.apply { preferredRefreshRate = 120f }
        enableEdgeToEdge()
        setContent {
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            MaterialTheme(colorScheme = lightColorScheme()) {
                Surface {
                    HomeScreen(
                        state = state,
                        onSearchChange = viewModel::onSearchChange,
                        onRoomChange = viewModel::onRoomChange,
                        onToggleDevice = viewModel::onToggleDevice
                    )
                }
            }
        }
    }
}
