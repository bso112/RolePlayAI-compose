package com.bso112.roleplayai.android.util


val String.Companion.Empty inline get() = ""
fun <T> List<T>.addFirst(item: T): List<T> {
    return listOf(item) + this
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