package com.bso112.domain

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

interface DataChangedEvent<T>

interface DataChangeNotifier {
    val dataChangedEvent: SharedFlow<DataChangedEvent<*>>
}

inline fun <reified T> DataChangeNotifier.autoRefreshFlow(
    crossinline block: suspend () -> T
) = merge(
    flow { emit(block()) },
    dataChangedEvent.filterIsInstance<DataChangedEvent<T>>().map {
        block()
    })