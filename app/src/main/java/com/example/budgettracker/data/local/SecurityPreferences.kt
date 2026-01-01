/*
package com.example.budgettracker.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "security_prefs")

class SecurityPreferences(private val context: Context) {

    private val INSECURE_ACCESS_KEY = booleanPreferencesKey("insecure_access_allowed")

    val isInsecureAccessAllowed: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[INSECURE_ACCESS_KEY] ?: false
        }

    suspend fun setInsecureAccessAllowed(allowed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[INSECURE_ACCESS_KEY] = allowed
        }
    }
}
*/
