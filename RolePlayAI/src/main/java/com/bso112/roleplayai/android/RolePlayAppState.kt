package com.bso112.roleplayai.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberRolePlayAIAppState(
    navController: NavHostController = rememberNavController()
): RolePlayAppState {
    return remember(
        navController
    ) {
        RolePlayAppState(
            navController = navController
        )
    }
}

class RolePlayAppState(
    val navController: NavHostController
) {
    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        navController.navigate(destination.route, destination.navOptions)
    }
}