package com.bso112.roleplayai.android.app

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.bso112.roleplayai.android.util.currentRoute

@Composable
fun RolePlayBottomNavigation(
    navController: NavController
) {
    var selectedItemIndex by remember { mutableStateOf(0) }
    BottomNavigation {
        TopLevelDestination.values().mapIndexed { index, type ->
            val isSelected = type.route == currentRoute(navController = navController)
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
                    navController.navigate(type.route, type.navOptions)
                }
            )
        }
    }
}