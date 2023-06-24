package com.bso112.data.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bso112.data.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreference(
    private val context: Context
) {
    private val data = context.dataStore.data

    private val userProfileIdKey = stringPreferencesKey("user_profile_id")

    val userId: Flow<String> = data.map { preferences ->
        preferences[userProfileIdKey].orEmpty()
    }

    suspend fun saveUserId(userId: String) {
        edit { setting -> setting[userProfileIdKey] = userId }
    }

    private suspend fun edit(transform: suspend (MutablePreferences) -> Unit) {
        context.dataStore.edit(transform)
    }

}