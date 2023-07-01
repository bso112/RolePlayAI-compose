package com.bso112.roleplayai.android.util

import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bso112.roleplayai.android.app.RolePlayAITheme
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


fun requireSdk(version: Int) = Build.VERSION.SDK_INT >= version

val randomID get() = UUID.randomUUID().toString()

