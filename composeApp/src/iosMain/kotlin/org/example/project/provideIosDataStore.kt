package org.example.project

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi // Add this
import org.example.project.repository.DATASTORE_FILE_NAME
import org.example.project.repository.createDataStore
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class) // Add this
fun provideIosDataStore(): DataStore<Preferences> =
    createDataStore {
        val directory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSApplicationSupportDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null
        )
        (directory?.path + "/${DATASTORE_FILE_NAME}")
    }