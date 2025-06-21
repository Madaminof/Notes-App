package com.example.todolist.composeUI.screen.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todolist.composeUI.navigation.Routes
import com.example.todolist.composeUI.screen.settings.UserProfil.UserProfileViewModel

@Composable
fun DrawerMenu(
    navController: NavController,
    onItemClick: () -> Unit,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsState()

    ModalDrawerSheet(
        modifier = Modifier
            .width(280.dp)
            .padding(end = 8.dp),
        drawerContainerColor = Color(0xFFF7F7F7)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Profil qismi
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            if (profile.avatarUri.isNotBlank()) {
                coil.compose.AsyncImage(
                    model = profile.avatarUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF448AFF),
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = profile.name.ifBlank { "Foydalanuvchi" },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )

            Text(
                text = profile.email.ifBlank { "email@example.com" },
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Menu elementlari
        DrawerItem("Bosh sahifa", Icons.Default.Home, Routes.HOME, navController, onItemClick)
        DrawerItem("Kalendar", Icons.Default.DateRange,Routes.CALENDAR,navController,onItemClick)
        DrawerItem("Eslatmalar", Icons.Default.Notifications, Routes.REMINDERS, navController, onItemClick)

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))

        DrawerItem("Sozlamalar", Icons.Default.Settings, Routes.SETTINGS, navController, onItemClick)
        DrawerItem("Ilova haqida", Icons.Default.Info, Routes.ABOUT, navController, onItemClick)
        DrawerItem("Chiqish", Icons.Default.ExitToApp, Routes.LOGOUT, navController, onItemClick)
    }
}


@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    route: String,
    navController: NavController,
    onClick: () -> Unit
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val selected = currentRoute == route

    NavigationDrawerItem(
        label = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                )
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (selected) Color(0xFF448AFF) else Color.Gray
            )
        },
        selected = selected,
        onClick = {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
            onClick()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color(0xFFE3F2FD),
            unselectedContainerColor = Color.Transparent,
            selectedIconColor = Color(0xFF448AFF),
            selectedTextColor = Color(0xFF448AFF)
        )
    )
}
