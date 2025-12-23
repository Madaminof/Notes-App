package com.example.todolist.composeUI.screen.Exit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.todolist.composeUI.Dialog.CustomDialog
import com.example.todolist.composeUI.navigation.Routes


@Composable
fun LogoutScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        CustomDialog(
            title = "Chiqish",
            message = "Profil ma’lumotlari tozalansinmi?",
            confirmText = "Ha",
            dismissText = "Yo‘q",
            onConfirm = {
                // Profilni tozalash logikasi
                showDialog = false

                // 🔄 Profilni tahrirlash sahifasiga yuborish
                navController.navigate(Routes.EDIT_PROFILE) {
                    popUpTo(0) { inclusive = true }
                }
            },
            onDismiss = {
                showDialog = false
                navController.popBackStack()
            }
        )
    }
}
