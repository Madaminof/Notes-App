package com.example.todolist.composeUI.Dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.todolist.R


@Composable
fun CustomDialog(
    title: String,
    message: String,
    confirmText: String = R.string.confirmText.toString(),
    dismissText: String =  R.string.dismissText.toString(),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        text = { Text(message, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(confirmText, color = MaterialTheme.colorScheme.onBackground)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(dismissText, color =MaterialTheme.colorScheme.onBackground)
            }
        }
    )
}
