package com.bso112.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import java.util.Date
import java.util.concurrent.TimeUnit


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun Long.toDateString(): String {
    val currentDate = Date()
    val diffInMillis = currentDate.time - this

    // Calculate the difference in years, months, days, hours, and minutes
    val diffInYears = TimeUnit.MILLISECONDS.toDays(diffInMillis) / 365
    val diffInMonths = TimeUnit.MILLISECONDS.toDays(diffInMillis) / 30
    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
    val diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)

    return when {
        diffInYears > 0 -> "$diffInYears years ago"
        diffInMonths > 0 -> "$diffInMonths months ago"
        diffInDays > 0 -> "$diffInDays days ago"
        diffInHours > 0 -> "$diffInHours hours ago"
        diffInMinutes > 0 -> "$diffInMinutes minutes ago"
        else -> "Just now"
    }
}

