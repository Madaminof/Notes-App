package com.example.todolist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = lightBlue,
    secondary = lightGren,
    tertiary = lightOrange,
    surface = lightKulrang,
    background = White,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = Black,
    onSurface = Black,


)

private val DarkColorScheme = darkColorScheme(
    primary = blackBlue,
    secondary = blackGren,
    tertiary = blackOrange,
    surface = blackKulrang,        // Yengil qora-yashil
    background = Black,     // Yorqin qora-ko‘k
    onPrimary = White,
    onSecondary = Black,
    onTertiary = Black,
    onBackground = White,
    onSurface = White,

)

@Composable
fun TodoListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
