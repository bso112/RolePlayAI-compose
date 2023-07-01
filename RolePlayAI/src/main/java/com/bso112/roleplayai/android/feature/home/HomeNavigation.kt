package com.bso112.roleplayai.android.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bso112.roleplayai.android.app.RolePlayAppState


const val HOME_ROUTE = "home"

fun NavController.navigateHome(navOptions: NavOptions? = null) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(appState: RolePlayAppState) {
    composable(HOME_ROUTE) {
        HomeScreenRoute(appState = appState)
    }
}