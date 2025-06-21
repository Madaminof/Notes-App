package com.example.todolist.composeUI.screen.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todolist.R

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    onNavigateToAddTask: () -> Unit // 🔥 qo‘shildi
) {
    val fabSize = 56.dp
    val fabOffset = (-fabSize / 2)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        NavigationBar(
            containerColor = Color(0xFFEAE8E8),
            tonalElevation = 20.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(75.dp)
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = stringResource(id = R.string.Menu)) },
                selected = selectedIndex == 0,
                onClick = { onItemSelected(0) },
                label = { Text("Home") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1976D2),
                    selectedTextColor = Color(0xFF1976D2),
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                icon = { Icon(Icons.Default.DateRange, contentDescription = stringResource(id = R.string.Calendar)) },
                selected = selectedIndex == 2,
                onClick = { onItemSelected(2) },
                label = { Text("Calendar") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1976D2),
                    selectedTextColor = Color(0xFF1976D2),
                    indicatorColor = Color.Transparent
                )
            )
        }

        // FAB tugmasi
        FloatingActionButton(
            onClick = { onNavigateToAddTask() }, // ✅ to‘g‘ridan-to‘g‘ri callback chaqirilyapti
            containerColor = Color(0xFF1976D2),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = fabOffset)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
        }
    }
}
