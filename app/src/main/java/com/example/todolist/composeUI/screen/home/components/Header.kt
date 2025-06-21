package com.example.todolist.composeUI.screen.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.todolist.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    title: String = "To-Do List",
    onMenuClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = {
            Text(
                text = title, // 💡 stringResource o‘rniga parametrdan foydalanildi
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.Menu),
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { /* notification bosildi */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(id = R.string.Notifications),
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* search bosildi */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.Search),
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF5886B4)
        ),
        scrollBehavior = scrollBehavior
    )
}
