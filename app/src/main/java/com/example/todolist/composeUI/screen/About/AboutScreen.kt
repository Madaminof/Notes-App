package com.example.todolist.composeUI.screen.About

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController,
    onMenuClick: () -> Unit // ⬅️ Drawer menyu tugmasi uchun parametr qo‘shildi
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ilova haqida",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onMenuClick() }) { // ⬅️ Drawer menyu tugmasi
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menyu",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5886B4) // Ko‘k header
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📋 ToDoList Ilovasi",
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ushbu ilova yordamida siz kundalik vazifalaringizni rejalashtirishingiz, eslatmalar yaratishingiz va samarali ishlashingiz mumkin.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray),
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Versiya: 1.0.0",
                        style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
                    )
                }
            }
        }
    }
}
