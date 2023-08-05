package com.bso112.roleplayai.android.app

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("ConflictingOnColor")
@Composable
fun RolePlayAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = Color(0xFF1B202D),
            primaryVariant = Color(0xFF7B8195),
            secondary = Color(0xFF1B202D),
            surface = Color(0xFF292F3F),
            onSurface = Color.White,
            onPrimary = Color.White
        )
    } else {
        lightColors(
            primary = Color(0xFF7758CC),
            primaryVariant = Color(0xFFE0D4FE),
            secondary = Color(0xFF7758CC),
            surface = Color.White,
            onSurface = Color.Black,
            onPrimary = Color.White
        )
    }
    val typography = Typography(
        body1 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

val Colors.placeHolder get() = Color.LightGray
val Colors.chatBubbleOther get() = if (isLight) Color(0xFFEBEBEB) else Color(0xFF383E4E)

val Colors.chatBubbleUser get() = primaryVariant

val Colors.highlightText get() = if (isLight) Color(0xFF7758CC) else Color.White

val Colors.chatTextFieldBackground get() = if(isLight) Color(0xFFF2ECFF) else Color(0xFFA6A9B6)

val Colors.caption get() = if(isLight) Color.LightGray else Color.White
val Colors.onChatBubbleOther get() = if (isLight) Color.Black else Color.White
val Colors.onChatBubbleUser get() = if (isLight) Color.Black else Color.White
val Colors.subText get() = if (isLight) Color.DarkGray else Color.LightGray
