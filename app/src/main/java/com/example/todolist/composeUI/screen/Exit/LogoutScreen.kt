package com.example.todolist.composeUI.screen.Exit

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.todolist.composeUI.navigation.Routes

@Composable
fun LogoutScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                navController.popBackStack() // 🔙 "Yo‘q" bosilganda orqaga qaytadi
            },
            title = { Text("Chiqish") },
            text = { Text("Profil ma’lumotlari tozalansinmi?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // ✅ Bu yerda siz profil ma’lumotlarini tozalovchi funksiyani chaqirasiz
                        showDialog = false

                        // Masalan: `UserPreferences.clear()` funksiyasi yoki ViewModel orqali
                        // TODO: Tozalash lozim bo'lgan joy

                        // 🔄 Profilni tahrirlash sahifasiga yuborish
                        navController.navigate(Routes.EDIT_PROFILE) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) {
                    Text("Ha")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.popBackStack()
                }) {
                    Text("Yo‘q")
                }
            }
        )
    }
}
