package com.example.week3_assignment.data

import com.example.week3_assignment.model.DeviceCategory
import com.example.week3_assignment.model.SmartDevice
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class DeviceRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val _devices = MutableStateFlow(
        listOf(
            SmartDevice(1, "Köşe Lambası", DeviceCategory.LIGHT, "Salon", true, true, 15.0),
            SmartDevice(2, "Tavan Avizesi", DeviceCategory.LIGHT, "Salon", true, false, 60.0),
            SmartDevice(3, "Bahçe Kamerası", DeviceCategory.CAMERA, "Bahçe", true, true, 8.5),
            SmartDevice(4, "Giriş Kamerası", DeviceCategory.CAMERA, "Antre", true, true, 8.5),
            SmartDevice(5, "Merkezi Termostat", DeviceCategory.THERMOSTAT, "Salon", true, true, 12.0),
            SmartDevice(6, "Yatak Odası Kliması", DeviceCategory.THERMOSTAT, "Yatak Odası", true, false, 1200.0),
            SmartDevice(7, "Mutfak Spotları", DeviceCategory.LIGHT, "Mutfak", true, false, 45.0),
            SmartDevice(8, "Roborock S7", DeviceCategory.VACUUM, "Koridor", true, true, 50.0),
            SmartDevice(9, "Balkon Aydınlatması", DeviceCategory.LIGHT, "Balkon", false, false, 25.0),
            SmartDevice(10, "Çalışma Odası Lambası", DeviceCategory.LIGHT, "Ofis", true, true, 12.0)
        )
    )
    val devices = _devices.asStateFlow()

    suspend fun toggleDevice(id: Int) = withContext(dispatcher) {
        _devices.update { devices ->
            devices.map { device ->
                if (device.id == id && device.isOnline) {
                    device.copy(isActive = !device.isActive)
                } else {
                    device
                }
            }
        }
    }
}
