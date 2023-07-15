package com.bso112.roleplayai.android.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.bso112.roleplayai.android.feature.chat.chatScreen
import com.bso112.roleplayai.android.feature.home.HOME_ROUTE
import com.bso112.roleplayai.android.feature.home.homeScreen
import com.bso112.roleplayai.android.feature.profile.createProfile

@Composable
fun RolePlayAIApp(
    appState: RolePlayAppState,
) {
    RolePlayAITheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = appState.snackBarHostState) },
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
        chatScreen(appState)
        createProfile(appState)
    }

}
