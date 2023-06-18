package com.bso112.roleplayai.android.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.bso112.roleplayai.android.feature.chat.CHAT_ROUTE
import com.bso112.roleplayai.android.feature.chat.chatScreen

@Composable
fun RolePlayAIApp(
    appState: RolePlayAppState = rememberRolePlayAIAppState()
) {
    RolePlayAITheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            RolePlayAINavHost(appState)
        }
    }
}

@Composable
fun HomeBottomNavigation(
    onClickNavItem: (TopLevelDestination) -> Unit
) {
    var selectedItemIndex by remember { mutableStateOf(0) }
    BottomNavigation {
        TopLevelDestination.values().mapIndexed { index, type ->
            val isSelected = index == selectedItemIndex
            BottomNavigationItem(
                selected = isSelected,
                icon = {
                    Icon(
                        imageVector = if (isSelected) type.selectedImage else type.deselectedImage,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = type.title)
                },
                onClick = {
                    selectedItemIndex = index
                    onClickNavItem(type)
                }
            )
        }
    }
}

@Composable
fun RolePlayAINavHost(
    appState: RolePlayAppState
) {
    NavHost(
        navController = appState.navController,
        startDestination = CHAT_ROUTE
    ) {
        chatScreen()
    }

}
