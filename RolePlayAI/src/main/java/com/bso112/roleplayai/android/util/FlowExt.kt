package com.bso112.roleplayai.android.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


fun <T, R> StateFlow<T>.mapState(
    externalScope: CoroutineScope,
    transform: (value: T) -> R
): StateFlow<R> {
    return map {
        transform(it)
    }.stateIn(externalScope, SharingStarted.Eagerly, transform(value))
}
