package com.bso112.roleplayai.android.util

import android.content.Context
import com.bso112.domain.Profile
import com.bso112.roleplayai.android.R
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

val fakeUser = Profile(
    id = UUID.randomUUID().toString(),
    thumbnail = "",
    name = "유저",
    description = "",
    firstMessage = "",
    singleLineDesc = ""
)

const val MENU_ITEM_ID_PAPAGO = 111111
const val MENU_ITEM_ID_GOOGLE = 16908353
const val PAPAGO_PACKAGE_NAME = "com.naver.labs.translator"
const val PAPAGO_MINI_ACTIVITY_NAME =
    "com.naver.labs.translator.ui.mini.control.ServiceStartActivity"


fun Long.toDateString(context: Context): String {
    val currentDate = Date()
    val diffInMillis = currentDate.time - this

    // Calculate the difference in years, months, days, hours, and minutes
    val diffInYears = TimeUnit.MILLISECONDS.toDays(diffInMillis) / 365
    val diffInMonths = TimeUnit.MILLISECONDS.toDays(diffInMillis) / 30
    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
    val diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)

    return when {
        diffInYears > 0 -> context.getString(R.string.time_years_ago, diffInYears)
        diffInMonths > 0 -> context.getString(R.string.time_months_ago, diffInMonths)
        diffInDays > 0 -> context.getString(R.string.time_days_ago, diffInDays)
        diffInHours > 0 -> context.getString(R.string.time_hours_ago, diffInHours)
        diffInMinutes > 0 -> context.getString(R.string.time_minutes_ago, diffInMinutes)
        else -> context.getString(R.string.time_just_now)
    }
}
