package com.example.todolist.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todolist.data.ReminderDao
import com.example.todolist.data.TaskDao
import com.example.todolist.data.TaskDatabase
import com.example.todolist.repository.ReminderRepository
import com.example.todolist.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ✅ Reminders jadvalini yaratish - oldingi migratsiya
    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS reminders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    text TEXT NOT NULL,
                    timeMillis INTEGER NOT NULL
                )
                """.trimIndent()
            )
        }
    }

    // ✅ Task jadvaliga isEdited ustunini qo‘shish
    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                ALTER TABLE tasks ADD COLUMN isEdited INTEGER NOT NULL DEFAULT 0
                """.trimIndent()
            )
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            "task_db"
        )
            .addMigrations(MIGRATION_3_4, MIGRATION_4_5)
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: TaskDatabase): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun provideReminderDao(database: TaskDatabase): ReminderDao = database.reminderDao()

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository = TaskRepository(taskDao)

    @Provides
    @Singleton
    fun provideReminderRepository(reminderDao: ReminderDao): ReminderRepository =
        ReminderRepository(reminderDao)
}
