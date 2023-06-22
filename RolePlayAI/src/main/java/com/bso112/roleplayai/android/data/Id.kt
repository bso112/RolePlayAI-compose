package com.bso112.roleplayai.android.data

import java.util.UUID

@JvmInline
value class Id(val id: String) {
    constructor() : this(UUID.randomUUID().toString())
}

fun String.toId() = Id(this)