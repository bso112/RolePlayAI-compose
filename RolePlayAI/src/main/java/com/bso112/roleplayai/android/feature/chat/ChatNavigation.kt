package com.bso112.roleplayai.android.feature.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bso112.roleplayai.android.app.RolePlayAppState

const val CHAT_ROUTE = "chat"

fun NavController.navigateChat(navOptions: NavOptions? = null){
    navigate(CHAT_ROUTE, navOptions)
}

fun NavGraphBuilder.chatScreen(appState: RolePlayAppState) {
    composable(CHAT_ROUTE) {
        ChatScreenRoute(appState)
    }
}