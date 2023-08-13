package com.bso112.data

import android.content.Context
import android.os.Parcelable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import java.io.File


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun Parcelable.writeToFile(context: Context, fileName: String) {
    File(context.filesDir, fileName).apply {
        if (!exists()) {
            val isCreated = createNewFile()
            if (!isCreated) {
                throw Exception("Failed to create file")
            }
        }
    }.also {
        it.writeText(Gson().toJson(this))
    }
}

