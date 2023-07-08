package com.bso112.util


val String.Companion.Empty inline get() = ""

suspend fun <T> result(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun <T> Collection<T>.filterIf(condition: Boolean, predicate: (T) -> Boolean): List<T> {
    return if (condition) {
        filter(predicate)
    } else {
        toList()
    }
}


suspend fun <T> T.alsoSuspend(block: suspend (T) -> Unit): T {
    return also {
        block(this)
    }
}

inline fun <reified T : Any> Any.ifIs(block: (T) -> Unit) {
    (this as? T)?.let(block)
}

fun <T> List<T>.sliceSafe(range: IntRange): List<T> {
    return slice(range.first.coerceAtLeast(0)..range.last.coerceAtMost(lastIndex))
}

fun <T> Collection<T>.sliceSafe(range: IntRange): List<T> {
    return toList().sliceSafe(range)
}

fun CharSequence.sliceSafe(range: IntRange): CharSequence {
    return slice(range.first.coerceAtLeast(0)..range.last.coerceAtMost(lastIndex))
}


fun <T> List<T>.add(item: T, index: Int = size): List<T> {
    return toMutableList().apply { add(index, item) }
}

fun <T> List<T>.replace(oldItem: T, newItem: T): List<T> {
    return map {
        if (it == oldItem) {
            newItem
        } else {
            it
        }
    }
}

fun <T> List<T>.update(selector: (T) -> Boolean, updater: (T) -> T): List<T> {
    return map {
        if (selector(it)) {
            updater(it)
        } else {
            it
        }
    }
}
