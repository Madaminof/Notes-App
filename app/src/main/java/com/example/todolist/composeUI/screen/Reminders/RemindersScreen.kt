package com.example.todolist.composeUI.screen.Reminders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todolist.composeUI.screen.home.components.DrawerMenu
import com.example.todolist.composeUI.screen.home.components.Header
import com.example.todolist.data.Reminder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    navController: NavController,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val reminders by viewModel.reminders.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedReminderForEdit by remember { mutableStateOf<Reminder?>(null) }

    LaunchedEffect(reminders) {
        viewModel.deleteExpiredReminders()
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val nestedScrollModifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)

    val cardColors = listOf(
        Color(0xFFE3F2FD),
        Color(0xFFFFF9C4),
        Color(0xFFE1BEE7),
        Color(0xFFC8E6C9)
    )

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
            modifier = nestedScrollModifier,
            topBar = {
                Header(
                    title = "Eslatmalar",
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxSize()
            ) {
                if (reminders.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 64.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔔", style = MaterialTheme.typography.displayLarge)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Eslatmalar yo‘q",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Yangi eslatma qo‘shish uchun + belgini bosing.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    itemsIndexed(reminders) { index, reminder ->
                        val bgColor = cardColors[index % cardColors.size]
                        var menuExpanded by remember { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(containerColor = bgColor),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = reminder.text,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                        modifier = Modifier.weight(1f),
                                        color = Color.Black
                                    )
                                    IconButton(onClick = { menuExpanded = true }) {
                                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray)
                                    }
                                    DropdownMenu(
                                        expanded = menuExpanded,
                                        onDismissRequest = { menuExpanded = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("✏️ Tahrirlash") },
                                            onClick = {
                                                selectedReminderForEdit = reminder
                                                menuExpanded = false
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("🗑 O‘chirish") },
                                            onClick = {
                                                viewModel.deleteReminder(reminder)
                                                menuExpanded = false
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = formatReminderDate(reminder.timeMillis),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    selectedReminderForEdit?.let { reminder ->
        ReminderEditDialog(
            reminder = reminder,
            onDismiss = { selectedReminderForEdit = null },
            onConfirm = { updated ->
                viewModel.updateReminder(updated)
                scheduleReminder(context, updated)
                selectedReminderForEdit = null
            }
        )
    }
}

fun formatReminderDate(millis: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(millis))
}
