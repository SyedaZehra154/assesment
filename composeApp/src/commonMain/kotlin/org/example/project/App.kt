package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.example.project.ui.MeterScreen
import org.example.project.viewmodel.MeterViewModel
import kotlinx.coroutines.MainScope

@Composable
fun App(dataStore: DataStore<Preferences>) {
    // Use a platform-independent CoroutineScope for the ViewModel
    val viewModelScope = remember { MainScope() }

    // Create the ViewModel only once
    val viewModel = remember { MeterViewModel(dataStore, viewModelScope) }

    MaterialTheme {
        MeterScreen(viewModel = viewModel)
    }

    // Dispose coroutine scope when this Composable is removed (important for iOS)
    DisposableEffect(Unit) {
        onDispose {
            viewModel.onCleared()
        }
    }
}