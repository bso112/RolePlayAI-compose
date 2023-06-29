package com.bso112.roleplayai.android.feature.profile

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navArgument
import com.bso112.domain.Profile
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.util.RouteBuilder
import com.bso112.roleplayai.android.util.createNavType


private const val CREATE_PROFILE_ROUTE = "CREATE_PROFILE_ROUTE"
private const val ARGS_PROFILE = "ARGS_PROFILE"

private val routeBuilder = RouteBuilder(
    path = CREATE_PROFILE_ROUTE,
    arguments = listOf(navArgument(ARGS_PROFILE) {
        nullable = true
        type = createNavType<Profile>(true)
    })
)

fun NavController.navigateCreateProfile(profile: Profile? = null, navOptions: NavOptions? = null) {
    navigate(routeBuilder.buildRoute(listOf(profile)), navOptions)
}

fun NavGraphBuilder.createProfile(appState: RolePlayAppState) {
    with(routeBuilder) {
        buildComposable {
            CreateProfileScreenRoute(appState = appState)
        }
    }
}

class CreateProfileArg(val profile: Profile?) {
    constructor(savedStateHandle: SavedStateHandle) : this(savedStateHandle[ARGS_PROFILE])
}