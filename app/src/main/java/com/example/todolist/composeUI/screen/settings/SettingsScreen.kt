package com.example.todolist.composeUI.screen.settings




import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.todolist.composeUI.navigation.Routes
import com.example.todolist.composeUI.screen.settings.UserProfil.UserProfileViewModel
import com.example.todolist.data.UserData.model.UserProfile
import com.example.todolist.ui.theme.BrandColor
import com.example.todolist.ui.theme.lightBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel(),
    onMenuClick: () -> Unit
) {
    val showClearDialog = remember { mutableStateOf(false) }
    val showLogoutDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val profileViewModel: UserProfileViewModel = hiltViewModel()
    val profile by profileViewModel.profile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Sozlamalar", color = MaterialTheme.colorScheme.onPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandColor)
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
            ProfileCard(profile = profile) {
                navController.navigate("profile_edit")
            }

            DangerItem(
                icon = Icons.Default.Delete,
                text = "Barcha topshiriqlarni o‘chirish",
                onClick = { showClearDialog.value = true }
            )

            DangerItem(
                icon = Icons.Default.Logout,
                text = "Chiqish",
                onClick = { showLogoutDialog.value = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Versiya: 1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        if (showClearDialog.value) {
            ConfirmDialog(
                title = "Diqqat!",
                message = "Barcha vazifalarni o‘chirishni xohlaysizmi?",
                onConfirm = {
                    scope.launch {
                        viewModel.clearAllTasks()
                        snackbarHostState.showSnackbar("Topshiriqlar o‘chirildi ✅")
                        showClearDialog.value = false

                        // 🧭 Yo‘naltirish:
                        navController.navigate(Routes.HOME) {
                            popUpTo(0) { inclusive = true } // barcha screenlarni tozalaydi
                        }
                    }
                },
                onDismiss = { showClearDialog.value = false }
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


@Composable
fun ProfileCard(profile: UserProfile, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (profile.avatarUri.isNotBlank()) {
                AsyncImage(
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
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(64.dp)
                        .background(lightBlue, CircleShape)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(profile.name.ifBlank { "Foydalanuvchi" }, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text(profile.email.ifBlank { "email@example.com" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Tahrirlash", tint = BrandColor)
            }
        }
    }
}


@Composable
fun DangerItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        tonalElevation = 3.dp,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}


@Composable
fun ConfirmDialog(title: String, message: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, color = MaterialTheme.colorScheme.onSurface) },
        text = { Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        confirmButton = {
            TextButton(onClick = onConfirm, colors = ButtonDefaults.textButtonColors(contentColor = BrandColor)) {
                Text("Ha")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Bekor qilish", color = MaterialTheme.colorScheme.outline)
            }
        }
    )
}
