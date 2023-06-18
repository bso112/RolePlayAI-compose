package com.bso112.roleplayai.android.util


fun <T> List<T>.addFirst(item: T): List<T> {
    return listOf(item) + this
}