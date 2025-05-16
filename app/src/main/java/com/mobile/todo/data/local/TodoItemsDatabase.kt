package com.mobile.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Item::class],
    version = 1,
    exportSchema = false
)
abstract class TodoItemsDatabase: RoomDatabase() {
    abstract fun itemsDao(): ItemDao

    companion object {
        @Volatile private var Instance: TodoItemsDatabase? = null

        fun getDatabase(context: Context): TodoItemsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TodoItemsDatabase::class.java, "todo_items_database")
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}