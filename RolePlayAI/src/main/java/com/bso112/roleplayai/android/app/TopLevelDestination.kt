package com.bso112.roleplayai.android.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavOptions
import com.bso112.roleplayai.android.feature.chat.CHAT_ROUTE

enum class TopLevelDestination(
    val selectedImage: ImageVector,
    val deselectedImage: ImageVector,
    val title: String,
    val route: String,
    val navOptions : NavOptions? = null
) {
    HOME(
        selectedImage = Icons.Filled.Favorite,
        deselectedImage = Icons.Outlined.Favorite,
        title = "홈",
        route = ""
    ),
    NEARBY(
        selectedImage = Icons.Filled.Favorite,
        deselectedImage = Icons.Outlined.Favorite,
        title = "내 근처",
        route = ""
    ),
    MY(
        selectedImage = Icons.Filled.Favorite,
        deselectedImage = Icons.Outlined.Favorite,
        title = "나의 당근",
        route = ""
    ),
    TOWN_LIFE(
        selectedImage = Icons.Filled.Favorite,
        deselectedImage = Icons.Outlined.Favorite,
        title = "동네생활",
        route = ""
    ),
    CHAT(
        selectedImage = Icons.Filled.Favorite,
        deselectedImage = Icons.Outlined.Favorite,
        title = "채팅",
        route = CHAT_ROUTE
    )
}
