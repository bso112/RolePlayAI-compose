package com.bso112.roleplayai.android.app

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberRolePlayAIAppState(
    navController: NavHostController = rememberNavController(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
): RolePlayAppState {
    return remember(
        navController,
        snackBarHostState,
    ) {
        RolePlayAppState(
            navController = navController,
            snackBarHostState = snackBarHostState,
        )
    }
}

class RolePlayAppState(
    val navController: NavHostController,
    val snackBarHostState: SnackbarHostState,
)