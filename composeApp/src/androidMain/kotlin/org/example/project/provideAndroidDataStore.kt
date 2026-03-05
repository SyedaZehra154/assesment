package org.example.project


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.example.project.repository.DATASTORE_FILE_NAME
import org.example.project.repository.createDataStore

fun provideAndroidDataStore(context: Context): DataStore<Preferences> =
    createDataStore {
        context.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath
    }