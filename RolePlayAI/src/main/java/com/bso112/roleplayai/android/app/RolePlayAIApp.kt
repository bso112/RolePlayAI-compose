package com.bso112.roleplayai.android.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bso112.roleplayai.android.feature.chat.CHAT_ROUTE
import com.bso112.roleplayai.android.feature.chat.chatScreen
import com.bso112.roleplayai.android.feature.chathistory.chatHistory
import com.bso112.roleplayai.android.feature.home.HOME_ROUTE
import com.bso112.roleplayai.android.feature.home.homeScreen
import com.bso112.roleplayai.android.feature.profile.createProfile

@Composable
fun RolePlayAIApp(
    appState: RolePlayAppState = rememberRolePlayAIAppState()
) {
    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()

    val excludeBottomBarRoute = listOf(CHAT_ROUTE)

    RolePlayAITheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                if (navBackStackEntry?.destination?.route.orEmpty() !in excludeBottomBarRoute) {
                    RolePlayBottomNavigation(appState.navController)
                }
            }
        ) { padding ->
            RolePlayAINavHost(Modifier.padding(padding), appState)
        }
    }
}


@Composable
fun RolePlayAINavHost(
    modifier: Modifier,
    appState: RolePlayAppState
) {
    NavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = HOME_ROUTE
    ) {
        homeScreen(appState)
        chatHistory(appState)
        chatScreen(appState)
        createProfile(appState)
    }

}
