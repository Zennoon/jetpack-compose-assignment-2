package com.mobile.todo.data

import android.content.Context
import com.mobile.todo.data.repository.TodoItemsRepository
import com.mobile.todo.data.local.ItemDao
import com.mobile.todo.data.local.TodoItemsDatabase
import com.mobile.todo.data.remote.TodoItemsApiService
import com.mobile.todo.data.repository.TodoItemsRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val todoItemsRepository: TodoItemsRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    private val baseUrl = "https://jsonplaceholder.typicode.com"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
    private val retrofitService: TodoItemsApiService by lazy {
        retrofit.create(TodoItemsApiService::class.java)
    }

    private val itemDao: ItemDao by lazy {
        TodoItemsDatabase.getDatabase(context).itemsDao()
    }

    override val todoItemsRepository: TodoItemsRepository by lazy {
        TodoItemsRepositoryImpl(retrofitService, itemDao)
    }
}