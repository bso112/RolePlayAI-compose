package com.bso112.roleplayai.android.util

import com.bso112.domain.Profile
import java.util.UUID

val fakeUser = Profile(
    id = UUID.randomUUID().toString(),
    thumbnail = "",
    name = "유저",
    description = "",
    firstMessage = ""
)

const val MENU_ITEM_ID_GOOGLE =  16908353
const val PAPAGO_PACKAGE_NAME = "com.naver.labs.translator"
const val PAPAGO_MINI_ACTIVITY_NAME = "com.naver.labs.translator.ui.mini.control.ServiceStartActivity"
