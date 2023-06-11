package com.bso112.roleplayai

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform