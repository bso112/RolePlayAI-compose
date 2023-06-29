package com.bso112.roleplayai.android.util

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson

inline fun <reified T : Parcelable> createNavType(isNullableAllowed: Boolean): NavType<T?> =
    object : NavType<T?>(isNullableAllowed) {
        override val name: String = checkNotNull(T::class.simpleName)

        override fun get(bundle: Bundle, key: String): T? {
            return if (requireSdk(33)) {
                bundle.getParcelable(key, T::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }

        override fun parseValue(value: String): T? {
            return kotlin.runCatching {
                Gson().fromJson(Uri.decode(value), T::class.java)
            }.getOrNull()
        }

        override fun put(bundle: Bundle, key: String, value: T?) {
            value?.let { bundle.putParcelable(key, it) }
        }

    }