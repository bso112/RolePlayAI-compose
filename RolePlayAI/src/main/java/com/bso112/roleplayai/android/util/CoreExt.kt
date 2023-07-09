package com.bso112.roleplayai.android.util


val Char.Companion.Empty inline get() = ' '
val String.Companion.Empty inline get() = ""

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
    var isReplaced = false
    val newList = map {
        if (it == oldItem) {
            isReplaced = true
            newItem
        } else {
            it
        }
    }
    return if (isReplaced) newList else this
}

fun <T> List<T>.update(selector: (T) -> Boolean, updater: (T) -> T): List<T> {
    var isUpdated = false
    val newList = map {
        if (selector(it)) {
            isUpdated = true
            updater(it)
        } else {
            it
        }
    }
    return if (isUpdated) newList else this
}
