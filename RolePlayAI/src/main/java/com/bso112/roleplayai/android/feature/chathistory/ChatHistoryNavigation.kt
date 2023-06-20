package com.bso112.roleplayai.android.feature.chathistory

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bso112.roleplayai.android.app.RolePlayAppState


const val CHAT_HISTORY_ROUTE = "CHAT_HISTORY_ROUTE"

fun NavController.navigateChatHistory(navOptions: NavOptions? = null){
    navigate(CHAT_HISTORY_ROUTE, navOptions)
}

fun NavGraphBuilder.chatHistory(appState: RolePlayAppState){
    composable(CHAT_HISTORY_ROUTE){
        ChatHistoryRoute(appState = appState)
    }
}