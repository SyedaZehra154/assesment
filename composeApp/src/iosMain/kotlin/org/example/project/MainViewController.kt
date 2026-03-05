package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    AppWrapper()
}

@Composable
fun AppWrapper() {
    val dataStore = remember { provideIosDataStore() }
    App(dataStore = dataStore)
}