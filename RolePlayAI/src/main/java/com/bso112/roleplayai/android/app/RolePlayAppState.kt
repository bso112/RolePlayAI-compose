package com.bso112.roleplayai.android.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bso112.data.local.AppPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberRolePlayAIAppState(
    navController: NavHostController = rememberNavController(),
    stateRetainedScope: CoroutineScope,
    appPreference: AppPreference
): RolePlayAppState {
    return remember(
        navController
    ) {
        RolePlayAppState(
            navController = navController,
            stateRetainedScope = stateRetainedScope,
            appPreference = appPreference
        )
    }
}

class RolePlayAppState(
    val navController: NavHostController,
    private val stateRetainedScope: CoroutineScope,
    private val appPreference: AppPreference
) {
    val userId: StateFlow<String?> =
        appPreference.userId.asFlow().stateIn(stateRetainedScope, SharingStarted.Eagerly, null)

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        navController.navigate(destination.route, destination.navOptions)
    }
}