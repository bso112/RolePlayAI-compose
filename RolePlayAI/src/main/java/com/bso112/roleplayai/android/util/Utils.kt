package com.bso112.roleplayai.android.util

import android.content.Context
import android.os.Build
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
fun DefaultPreview(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    RolePlayAITheme {
        Surface(
            modifier = modifier,
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

