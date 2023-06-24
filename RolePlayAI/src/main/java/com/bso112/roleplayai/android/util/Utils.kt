package com.bso112.roleplayai.android.util

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.roleplayai.android.app.RolePlayAITheme
import kotlinx.coroutines.flow.Flow
import java.util.UUID


@Composable
fun DefaultPreview(content: @Composable () -> Unit) {
    RolePlayAITheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
            content = content
        )
    }
}


@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

fun ProfileRepository.getUser(): Flow<Profile> {
    val defaultUser = Profile(
        id = UUID.randomUUID().toString(),
        thumbnail = "",
        name = "유저",
        description = ""
    )
    return getUser(defaultUser)
}

val randomID get() = UUID.randomUUID().toString()