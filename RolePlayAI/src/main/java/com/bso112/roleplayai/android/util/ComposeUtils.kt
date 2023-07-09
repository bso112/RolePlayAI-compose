package com.bso112.roleplayai.android.util

import androidx.compose.material.DropdownMenu
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bso112.roleplayai.android.app.RolePlayAITheme


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


@Composable
fun OverflowMenu(iconContent: @Composable () -> Unit, menuItemContent: @Composable () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    IconButton(onClick = {
        showMenu = !showMenu
    }) {
        iconContent()
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        menuItemContent()
    }
}


