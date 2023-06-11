package com.bso112.roleplayai.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bso112.roleplayai.android.feature.main.navigateHome

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
        when (destination) {
            TopLevelDestination.HOME -> navController.navigateHome()
            TopLevelDestination.NEARBY -> navController.navigate("nearby")
            TopLevelDestination.MY -> navController.navigate("my")
            TopLevelDestination.TOWN_LIFE -> navController.navigate("town_life")
            TopLevelDestination.CHAT -> navController.navigate("chat")
        }
    }
}