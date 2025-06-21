package com.example.todolist.composeUI.screen.Reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todolist.data.Reminder

fun scheduleReminder(context: Context, reminder: Reminder) {
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("title", reminder.text)
    }

    // ⚠️ reminder.id bo‘sh bo‘lishi yoki 0 bo‘lishi mumkin, shu sababli unique requestCode ishlatamiz
    val requestCode = (reminder.timeMillis / 1000L).toInt()

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // ✅ setExactAndAllowWhileIdle - Android 6+ uchun ishonchli
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        reminder.timeMillis,
        pendingIntent
    )
}
