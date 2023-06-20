package com.bso112.roleplayai.android.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavOptions
import com.bso112.roleplayai.android.feature.chathistory.CHAT_HISTORY_ROUTE
import com.bso112.roleplayai.android.feature.home.HOME_ROUTE

enum class TopLevelDestination(
    val selectedImage: ImageVector,
    val deselectedImage: ImageVector,
    val title: String,
    val route: String,
    val navOptions: NavOptions? = null
) {
    HOME(
        selectedImage = Icons.Filled.Favorite,
        deselectedImage = Icons.Outlined.Favorite,
        title = "홈",
        route = HOME_ROUTE
    ),
    CHAT_HISTORY(
        selectedImage = Icons.Filled.Favorite,
        deselectedImage = Icons.Outlined.Favorite,
        title = "채팅 목록",
        route = CHAT_HISTORY_ROUTE
    )
}
