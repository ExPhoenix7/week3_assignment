package com.example.week3_assignment.ui

data class DeviceUiState(
    val id: Int,
    val name: String,
    val description: String,
    val status: String,
    val watts: String,
    val buttonText: String,
    val isOnline: Boolean
)

data class HomeUiState(
    val searchQuery: String = "",
    val selectedRoom: String = "Tümü",
    val rooms: List<String> = listOf("Tümü"),
    val devices: List<DeviceUiState> = emptyList(),
    val totalPower: String = "...",
    val isLoading: Boolean = true
)
