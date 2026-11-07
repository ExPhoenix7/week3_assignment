package com.example.week3_assignment.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    state: HomeUiState,
    onSearchChange: (String) -> Unit,
    onRoomChange: (String) -> Unit,
    onToggleDevice: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding()
            .navigationBarsPadding().imePadding().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Akıllı Ev Kontrol Merkezi", style = MaterialTheme.typography.titleLarge)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Anlık enerji tüketimi")
                Text(
                    state.totalPower,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("Açık ve çevrimiçi cihazlar", style = MaterialTheme.typography.bodySmall)
            }
        }

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = onSearchChange,
            label = { Text("Cihaz ara") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.rooms, key = { it }) { room ->
                FilterChip(
                    selected = state.selectedRoom == room,
                    onClick = { onRoomChange(room) },
                    label = { Text(room) }
                )
            }
        }

        Text("Cihazlar", style = MaterialTheme.typography.titleMedium)
        when {
            state.isLoading -> Text("Yükleniyor...")
            state.devices.isEmpty() -> Text("Cihaz bulunamadı.")
            else -> LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(state.devices, key = { it.id }) { device ->
                    DeviceCard(device, onToggleDevice)
                }
            }
        }
    }
}

@Composable
private fun DeviceCard(device: DeviceUiState, onToggleDevice: (Int) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(device.name, fontWeight = FontWeight.SemiBold)
                Text(device.description, style = MaterialTheme.typography.bodySmall)
                Text(device.status, style = MaterialTheme.typography.bodySmall)
                Text(device.watts, style = MaterialTheme.typography.bodySmall)
            }
            Button(
                onClick = { onToggleDevice(device.id) },
                enabled = device.isOnline
            ) {
                Text(device.buttonText)
            }
        }
    }
}
