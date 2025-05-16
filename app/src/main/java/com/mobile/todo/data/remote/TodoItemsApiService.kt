package com.mobile.todo.data.remote

import com.mobile.todo.data.local.Item
import retrofit2.http.GET
import retrofit2.http.Path

interface TodoItemsApiService {
    @GET("/todos")
    suspend fun getAllItems(): List<Item>

    @GET("/todos/{id}")
    suspend fun getItem(@Path("id") id: Int): Item
}