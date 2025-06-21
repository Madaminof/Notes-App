package com.example.todolist.composeUI.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todolist.data.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskCard(
    task: Task,
    onDelete: () -> Unit,
    onEdit: (Task) -> Unit, // ✅ Bu yerda Task beriladi
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = rememberFormattedDate(task.date)
    var menuExpanded by remember { mutableStateOf(false) }

    val (backgroundColor, badge, badgeColor) = when {
        task.isEdited -> Triple(Color(0xFFFFF8E1), "✏️ Tahrirlangan", Color(0xFFFFA000))
        isToday(task.date) -> Triple(Color(0xFFE3F2FD), "🕒 Bugun", Color(0xFF448AFF))
        isWithinAWeek(task.date) -> Triple(Color(0xFFCDF3DD), "📅 Haftalik", Color(0xFF4CAF50))
        else -> Triple(Color.White, "🗂 Eski", Color.Gray)
    }

    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                )

                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Ko‘proq",
                            tint = Color.Gray
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Tahrirlash") },
                            onClick = {
                                menuExpanded = false
                                onEdit(task) // ✅ TASK NI UZATISH
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("O‘chirish") },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            if (task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = badgeColor
                )
            }
        }
    }
}


@Composable
fun rememberFormattedDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun isToday(timestamp: Long): Boolean {
    val now = System.currentTimeMillis()
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return sdf.format(now) == sdf.format(Date(timestamp))
}

fun isWithinAWeek(timestamp: Long): Boolean {
    val now = System.currentTimeMillis()
    val oneWeekMillis = 7 * 24 * 60 * 60 * 1000
    return now - timestamp in 1..oneWeekMillis
}