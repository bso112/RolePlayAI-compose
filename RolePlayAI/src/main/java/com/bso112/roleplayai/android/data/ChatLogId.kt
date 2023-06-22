package com.bso112.roleplayai.android.data

import java.util.UUID

@JvmInline
value class ChatLogId(val id: String) {
    constructor() : this(UUID.randomUUID().toString())
}