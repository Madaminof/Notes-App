package com.example.todolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY date DESC")
    fun getAllTasks(): Flow<List<Task>>

    // 🆕 Barchasini o‘chirish
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()

}





@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder)
    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("SELECT * FROM reminders ORDER BY timeMillis ASC")
    fun getAllReminders(): Flow<List<Reminder>>

    @Query("DELETE FROM reminders WHERE timeMillis < :currentTime")
    suspend fun deleteOldReminders(currentTime: Long)
}
