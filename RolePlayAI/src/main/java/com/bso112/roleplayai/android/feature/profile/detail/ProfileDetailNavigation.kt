package com.bso112.roleplayai.android.feature.profile.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navArgument
import com.bso112.domain.Profile
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.util.RouteBuilder
import com.bso112.roleplayai.android.util.createNavType


private const val PROFILE_DETAIL_ROUTE = "CREATE_PROFILE_ROUTE"
private const val ARGS_PROFILE = "ARGS_PROFILE"

private val routeBuilder = RouteBuilder(
    path = PROFILE_DETAIL_ROUTE,
    arguments = listOf(navArgument(ARGS_PROFILE) {
        nullable = false
        type = createNavType<Profile>(false)
    })
)

fun NavController.navigateProfileDetail(profile: Profile, navOptions: NavOptions? = null) {
    navigate(routeBuilder.buildRoute(listOf(profile)), navOptions)
}

fun NavGraphBuilder.profileDetail(appState: RolePlayAppState) {
    with(routeBuilder) {
        buildComposable {
            ProfileDetailRoute(appState = appState)
        }
    }
}

internal class ProfileDetailArg(val profile: Profile) {
    constructor(
        savedStateHandle: SavedStateHandle
    ) : this(checkNotNull(savedStateHandle.get<Profile>(ARGS_PROFILE)))
}
