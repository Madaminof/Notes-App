package com.example.todolist.composeUI.screen.Details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.composeUI.screen.EditScreen.TaskEditDialog
import com.example.todolist.composeUI.screen.home.HomeViewModel
import com.example.todolist.data.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavController,
    task: Task,
    viewModel: HomeViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        TaskEditDialog(
            task = task,
            onDismiss = { showDialog = false },
            onConfirm = { updatedTask ->
                viewModel.updateTask(updatedTask)
                showDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Vazifa tafsilotlari",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White, // ✅ Oq matn
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White // ✅ Oq ikon
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.White // ✅ Oq ikon
                        )
                    }
                    IconButton(onClick = {
                        viewModel.deleteTask(task)
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White // ✅ Oq ikon
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5886B4) // ✅ Telegram-style moviy fon
                )
            )
        },
        containerColor = Color(0xFFF7F7F7)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 4.dp,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )

                    if (task.description.isNotBlank()) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.DarkGray
                        )
                    }

                    Divider(color = Color.LightGray)

                    Text(
                        text = "Created: ${formatDate(task.date)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )

                    if (task.isEdited) {
                        Text(
                            text = "✏️ Tahrirlangan",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFFF57C00)
                        )
                    }
                }
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
