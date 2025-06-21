package com.example.todolist.composeUI.screen.Reminders

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.todolist.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Eslatma!"

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 🔊 Custom tovush: raw/reminder_sound.mp3
        val soundUri: Uri = Uri.parse("android.resource://${context.packageName}/${R.raw.music}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()

                setSound(soundUri, audioAttributes)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("⏰ Eslatma")
            .setContentText(title)
            .setSound(soundUri) // 👈 bu pastki versiyalar uchun
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
