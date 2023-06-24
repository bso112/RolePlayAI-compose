package com.bso112.roleplayai.android.util

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.bso112.domain.Profile
import com.google.gson.Gson

val NAV_TYPE_PROFILE = createNavType<Profile>(false)

inline fun <reified T : Parcelable> createNavType(isNullableAllowed: Boolean): NavType<T> =
    object : NavType<T>(isNullableAllowed) {
        override val name: String = checkNotNull(T::class.simpleName)

        override fun get(bundle: Bundle, key: String): T? {
            return if (requireSdk(33)) {
                bundle.getParcelable(key, T::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }

        override fun parseValue(value: String): T {
            return Gson().fromJson(value, T::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putParcelable(key, value)
        }

    }