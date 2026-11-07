package com.example.week3_assignment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week3_assignment.data.DeviceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class HomeViewModel(
    private val repository: DeviceRepository = DeviceRepository(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val selectedRoom = MutableStateFlow("Tümü")
    private val turkishLocale = Locale.forLanguageTag("tr-TR")

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repository.devices, searchQuery, selectedRoom) { devices, query, room ->
                val search = query.trim().lowercase(turkishLocale)
                val filteredDevices = devices.filter { device ->
                    (room == "Tümü" || device.room == room) &&
                        device.name.lowercase(turkishLocale).contains(search)
                }

                val total = devices.filter { it.isOnline && it.isActive }.sumOf { it.watts }

                HomeUiState(
                    searchQuery = query,
                    selectedRoom = room,
                    rooms = listOf("Tümü") + devices.map { it.room }.distinct(),
                    devices = filteredDevices.map { device ->
                        DeviceUiState(
                            id = device.id,
                            name = device.name,
                            description = "${device.room} · ${device.category.name}",
                            status = when {
                                !device.isOnline -> "Çevrimdışı"
                                device.isActive -> "Çevrimiçi · Açık"
                                else -> "Çevrimiçi · Kapalı"
                            },
                            watts = formatWatts(device.watts),
                            buttonText = if (device.isActive) "Kapat" else "Aç",
                            isOnline = device.isOnline
                        )
                    },
                    totalPower = formatWatts(total),
                    isLoading = false
                )
            }.flowOn(dispatcher).collect { result ->
                _uiState.update { current ->
                    if (current.searchQuery == result.searchQuery &&
                        current.selectedRoom == result.selectedRoom
                    ) result else current
                }
            }
        }
    }

    fun onSearchChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchQuery.value = query
    }

    fun onRoomChange(room: String) {
        _uiState.update { it.copy(selectedRoom = room) }
        selectedRoom.value = room
    }

    fun onToggleDevice(id: Int) {
        viewModelScope.launch {
            repository.toggleDevice(id)
        }
    }

    private fun formatWatts(watts: Double): String =
        String.format(turkishLocale, "%.1f W", watts)
}
