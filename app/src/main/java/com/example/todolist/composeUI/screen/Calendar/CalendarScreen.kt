package com.example.todolist.composeUI.screen.Calendar

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.composeUI.screen.home.HomeViewModel
import com.example.todolist.composeUI.screen.home.components.DrawerMenu
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(currentDate()) }

    val tasks by viewModel.tasks.collectAsState()

    // ✅ Formatlarni to‘g‘ri taqqoslaymiz
    val filteredTasks = tasks.filter {
        formatDate(it.date) == selectedDate
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                navController = navController,
                onItemClick = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Kalendar",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Dehaze, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            showDatePicker(context) { pickedDate ->
                                selectedDate = pickedDate
                            }
                        }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Sana tanlash", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF5886B4))
                )
            },
            containerColor = Color(0xFFF7F7F7)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Tanlangan sana: $selectedDate",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (filteredTasks.isEmpty()) {
                    Text(
                        "Ushbu sanada hech qanday vazifa yo'q.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    filteredTasks.forEach { task ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = {
                                navController.navigate("task_detail/${task.id}")
                            }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(task.title, style = MaterialTheme.typography.titleMedium)
                                if (task.description.isNotBlank()) {
                                    Text(task.description, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun currentDate(): String {
    return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
}

fun formatDate(timeMillis: Long): String {
    return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(timeMillis))
}

fun showDatePicker(context: Context, onDatePicked: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    android.app.DatePickerDialog(context, { _, y, m, d ->
        val formatted = String.format("%02d-%02d-%04d", d, m + 1, y)
        onDatePicked(formatted)
    }, year, month, day).show()
}

