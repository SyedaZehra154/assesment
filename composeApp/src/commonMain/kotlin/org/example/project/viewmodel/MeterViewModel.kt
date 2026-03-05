package org.example.project.viewmodel

import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.domain.*

class MeterViewModel(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope = MainScope() // default scope works on Android & iOS
) {

    private val HISTORY_KEY = stringPreferencesKey("meter_history")

    private val slabs = listOf(
        Slab(1, 100, 5.0),
        Slab(101, 500, 8.0),
        Slab(501, null, 10.0)
    )

    private val calculator = ElectricityCalculator(slabs)

    var cost by mutableStateOf<Double?>(null)
    var error by mutableStateOf<String?>(null)
    var history by mutableStateOf<List<MeterReading>>(emptyList())

    init {
        // Collect from DataStore and update the UI state automatically
        scope.launch {
            dataStore.data
                .map { preferences ->
                    val json = preferences[HISTORY_KEY] ?: "[]"
                    try {
                        Json.decodeFromString<List<MeterReading>>(json)
                    } catch (e: Exception) {
                        emptyList()
                    }
                }
                .collect { savedHistory ->
                    history = savedHistory
                }
        }
    }

    fun updateError(message: String?) {
        error = message
        if (message != null) cost = null
    }

    fun submit(service: String, reading: Int) {
        if (!service.matches(Regex("^[a-zA-Z0-9]{10}$"))) {
            updateError("Service number must be 10 alphanumeric characters")
            return
        }

        scope.launch {
            val previous = history.firstOrNull { it.serviceNumber == service }

            val consumption = if (previous == null) {
                reading
            } else {
                if (reading < previous.reading) {
                    updateError("New reading cannot be less than previous (${previous.reading})")
                    return@launch
                }
                reading - previous.reading
            }

            cost = calculator.calculate(consumption)
            error = null
        }
    }

    fun save(service: String, reading: Int) {
        val bill = cost ?: return
        val record = MeterReading(serviceNumber = service, reading = reading, cost = bill)

        scope.launch {
            try {
                dataStore.edit { preferences ->
                    val currentJson = preferences[HISTORY_KEY] ?: "[]"
                    val currentList = try {
                        Json.decodeFromString<List<MeterReading>>(currentJson).toMutableList()
                    } catch (e: Exception) {
                        mutableListOf()
                    }

                    currentList.add(0, record)

                    val updatedList = currentList.take(10)
                    preferences[HISTORY_KEY] = Json.encodeToString(updatedList)
                }
            } catch (e: Exception) {
                // optional: handle DataStore errors
            }

            cost = null
            error = null
        }
    }


    fun onCleared() {
        scope.cancel()
    }
}