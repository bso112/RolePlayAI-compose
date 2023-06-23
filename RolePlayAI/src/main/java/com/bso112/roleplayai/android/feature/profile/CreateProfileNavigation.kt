package com.bso112.roleplayai.android.feature.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bso112.roleplayai.android.app.RolePlayAppState


const val CREATE_PROFILE_ROUTE = "CREATE_PROFILE_ROUTE"

fun NavController.navigateCreateProfile(navOptions: NavOptions? = null) {
    navigate(CREATE_PROFILE_ROUTE, navOptions)
}

fun NavGraphBuilder.createProfile(appState: RolePlayAppState) {
    composable(CREATE_PROFILE_ROUTE) {
        CreateProfileScreenRoute(appState = appState)
    }
}