package com.bso112.roleplayai.android.feature.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val CHAT_ROUTE = "chat"

fun NavController.navigateChat(navOptions: NavOptions? = null){
    navigate(CHAT_ROUTE, navOptions)
}

fun NavGraphBuilder.chatScreen() {
    composable(CHAT_ROUTE) {
        ChatScreenRoute()
    }
}