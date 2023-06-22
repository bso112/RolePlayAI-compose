package com.bso112.roleplayai.android.feature.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.data.Id

const val CHAT_ROUTE = "chat"
const val ARG_PROFILE_ID = "KEY_PROFILE_ID"
const val ARG_CHAT_LOG_ID = "KEY_CHAT_LOG_ID"

fun NavController.navigateChat(
    profileId: Id,
    chatLogId: Id? = null,
    navOptions: NavOptions? = null
) {
    navigate("$CHAT_ROUTE?$ARG_PROFILE_ID=${profileId.id}&$ARG_CHAT_LOG_ID=${chatLogId?.id}", navOptions)
}

fun NavGraphBuilder.chatScreen(appState: RolePlayAppState) {
    composable(
        "$CHAT_ROUTE?$ARG_PROFILE_ID={$ARG_PROFILE_ID}&$ARG_CHAT_LOG_ID={$ARG_CHAT_LOG_ID}",
        arguments = listOf(
            navArgument(ARG_PROFILE_ID) { nullable = false },
            navArgument(ARG_CHAT_LOG_ID) { nullable = true })
    ) {
        ChatScreenRoute(appState)
    }
}