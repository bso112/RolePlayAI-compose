package com.bso112.roleplayai.android.app

import androidx.compose.material.SnackbarHostState
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
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    stateRetainedScope: CoroutineScope,
    appPreference: AppPreference
): RolePlayAppState {
    return remember(
        navController,
        snackBarHostState,
    ) {
        RolePlayAppState(
            navController = navController,
            snackBarHostState = snackBarHostState,
            stateRetainedScope = stateRetainedScope,
            appPreference = appPreference
        )
    }
}

class RolePlayAppState(
    val navController: NavHostController,
    val snackBarHostState: SnackbarHostState,
    private val stateRetainedScope: CoroutineScope,
    private val appPreference: AppPreference
) {
    val userId: StateFlow<String?> =
        appPreference.userId.asFlow()
            .stateIn(stateRetainedScope, SharingStarted.Eagerly, null)
}