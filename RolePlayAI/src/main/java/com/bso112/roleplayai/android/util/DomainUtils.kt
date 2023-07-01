package com.bso112.roleplayai.android.util

import com.bso112.domain.Profile
import java.util.UUID

val fakeUser = Profile(
    id = UUID.randomUUID().toString(),
    thumbnail = "",
    name = "유저",
    description = ""
)

