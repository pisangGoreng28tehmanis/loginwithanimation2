package com.dicoding.picodiploma.loginwithanimation.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

// Create the DataStore delegate
private val Context.dataStore by preferencesDataStore(name = "my_datastore")

class MyDataStore(private val context: Context) {

    // Key for the token
    companion object {
        private val KEY_TOKEN = stringPreferencesKey("KEY_TOKEN")
    }

    // Retrieve the token as a Flow
    val tokenFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            // Handle exceptions during data access
            exception.printStackTrace()
        }
        .map { preferences ->
            preferences[KEY_TOKEN] ?: ""
        }

    // Save the token
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
        }
    }
}
