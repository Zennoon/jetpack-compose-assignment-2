package com.mobile.todo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<Item>)

    @Update
    suspend fun update(item: Item)

    @Query("SELECT * FROM todo_items")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM todo_items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>
}