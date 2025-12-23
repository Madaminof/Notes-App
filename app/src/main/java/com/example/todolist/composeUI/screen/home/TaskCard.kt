package com.example.todolist.composeUI.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.data.Task
import com.example.todolist.ui.theme.blue
import com.example.todolist.ui.theme.green
import com.example.todolist.ui.theme.orange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun TaskCard(
    task: Task,
    onDelete: () -> Unit,
    onEdit: (Task) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = rememberFormattedDate(task.date)
    var menuExpanded by remember { mutableStateOf(false) }

    val colorScheme = MaterialTheme.colorScheme

    val (backgroundColor, badgeText, badgeColor) = when {
        task.isEdited -> Triple(
            MaterialTheme.colorScheme.tertiary,           // 📌 custom color from Color.kt
            "✏️ Tahrirlangan",
            orange
        )

        isToday(task.date) -> Triple(
            MaterialTheme.colorScheme.primary,
            "🕒 Bugun",
            blue
        )

        isWithinAWeek(task.date) -> Triple(
            MaterialTheme.colorScheme.secondary,
            "📅 Haftalik",
            green
        )

        else -> Triple(
            colorScheme.surface,
            "🗂 Eski",
            colorScheme.outline
        )
    }

    Surface(
        modifier = modifier
            .clickable(
                indication = null, // 📌 ripple ko‘rinmasin
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // 🔝 Title + Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,                        // kattaroq matn
                        fontWeight = FontWeight.SemiBold,        // yarim qalin
                        lineHeight = 26.sp,                      // qulay o'qish uchun oraliq
                        letterSpacing = 0.1.sp,                  // ozgina harf oraliq
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,            // juda uzun bo‘lsa "..." bilan kesiladi
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                )


                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Ko‘proq",
                            tint = MaterialTheme.colorScheme.onBackground
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
                                onEdit(task)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(
                                "O‘chirish",
                                color = MaterialTheme.colorScheme.error
                            ) },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            }
                        )
                    }
                }

            }

            // 📝 Description
            if (task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.padding(6.dp))

            // 📅 Sana + Badge
            Column {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
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
