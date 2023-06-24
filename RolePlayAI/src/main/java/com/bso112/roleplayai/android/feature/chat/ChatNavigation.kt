package com.bso112.roleplayai.android.feature.chat

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navArgument
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.util.RouteBuilder

const val CHAT_ROUTE = "chat"
private const val ARG_PROFILE_ID = "KEY_PROFILE_ID"
private const val ARG_CHAT_LOG_ID = "KEY_CHAT_LOG_ID"

private val routeBuilder = RouteBuilder(
    path = CHAT_ROUTE,
    arguments = listOf(
        navArgument(ARG_PROFILE_ID) {
            nullable = false
        },
        navArgument(ARG_CHAT_LOG_ID) {
            nullable = true
        }
    )
)

fun NavController.navigateChat(
    profileId: String,
    chatLogId: String? = null,
    navOptions: NavOptions? = null
) {
    navigate(routeBuilder.buildRoute(listOf(profileId, chatLogId)), navOptions)
}

fun NavGraphBuilder.chatScreen(appState: RolePlayAppState) {
    with(routeBuilder) {
        buildComposable {
            ChatScreenRoute(appState)
        }
    }
}

internal class ChatScreenArg(val profileId: String, val chatLogId: String?) {
    constructor(
        savedStateHandle: SavedStateHandle
    ) : this(
        checkNotNull(savedStateHandle.get<String>(ARG_PROFILE_ID)),
        savedStateHandle.get<String>(ARG_CHAT_LOG_ID)
    )
}