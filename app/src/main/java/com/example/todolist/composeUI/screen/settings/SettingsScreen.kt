package com.example.todolist.composeUI.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todolist.composeUI.screen.settings.UserProfil.UserProfileViewModel
import com.example.todolist.data.UserData.model.UserProfile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel(),
    onMenuClick: () -> Unit
) {
    val isNotificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val showLogoutDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val userProfileViewModel: UserProfileViewModel = hiltViewModel()
    val profile by userProfileViewModel.profile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sozlamalar",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF5886B4))
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 👤 Foydalanuvchi kartasi
            ProfileCard(profile = profile, onEdit = {
                navController.navigate("profile_edit")
            })

            // 🔔 Bildirishnomalar
            SettingItemSwitch(
                title = "🔔 Bildirishnomalar",
                checked = isNotificationsEnabled,
                onCheckedChange = { viewModel.toggleNotifications(it) }
            )

            // 🗑 Topshiriqlarni tozalash
            DangerItem(
                text = "🗑 Barcha topshiriqlarni o‘chirish",
                onClick = { showDialog.value = true },
                backgroundColor = Color(0xFFFFF3E0),
                textColor = Color(0xFFE65100)
            )

            // 🚪 Chiqish
            DangerItem(
                text = "🚪 Chiqish",
                onClick = { showLogoutDialog.value = true },
                backgroundColor = Color(0xFFFFEBEE),
                textColor = Color.Red
            )

            // 🧾 Dialoglar
            if (showDialog.value) {
                ConfirmDialog(
                    title = "Diqqat!",
                    message = "Barcha vazifalarni o‘chirishni xohlaysizmi?",
                    onConfirm = {
                        scope.launch {
                            viewModel.clearAllTasks()
                            snackbarHostState.showSnackbar("Topshiriqlar o‘chirildi ✅")
                            showDialog.value = false
                        }
                    },
                    onDismiss = { showDialog.value = false }
                )
            }

            if (showLogoutDialog.value) {
                ConfirmDialog(
                    title = "Chiqish",
                    message = "Chiqishni xohlaysizmi? Profil ma'lumotlari o‘chiriladi.",
                    onConfirm = {
                        viewModel.logout {
                            showLogoutDialog.value = false
                            navController.navigate("profile_edit") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    onDismiss = { showLogoutDialog.value = false }
                )
            }
        }
    }
}


@Composable
fun SettingItemSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun ProfileCard(profile: UserProfile, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (profile.avatarUri.isNotBlank()) {
                coil.compose.AsyncImage(
                    model = profile.avatarUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.Gray, CircleShape)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(profile.name.ifBlank { "Foydalanuvchi" }, style = MaterialTheme.typography.titleMedium)
                Text(profile.email.ifBlank { "email@example.com" }, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Tahrirlash")
            }
        }
    }
}

@Composable
fun DangerItem(text: String, onClick: () -> Unit, backgroundColor: Color, textColor: Color) {
    Surface(
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = textColor,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Composable
fun ConfirmDialog(title: String, message: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Ha", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Bekor qilish")
            }
        }
    )
}

