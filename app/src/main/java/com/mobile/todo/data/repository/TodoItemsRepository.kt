package com.mobile.todo.data.repository

import android.util.Log
import com.mobile.todo.data.local.Item
import com.mobile.todo.data.local.ItemDao
import com.mobile.todo.data.models.Resource
import com.mobile.todo.data.remote.TodoItemsApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface TodoItemsRepository {
    fun getAllTodos(): Flow<Resource<List<Item>>>

    fun getTodo(id: Int): Flow<Resource<Item>>
}

class TodoItemsRepositoryImpl(
    private val todoItemsApiService: TodoItemsApiService,
    private val itemDao: ItemDao
): TodoItemsRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllTodos(): Flow<Resource<List<Item>>> = flow {
        emit(Resource.Loading())

        val localData = itemDao.getAllItems().firstOrNull()
        emit(Resource.Loading(data = localData))

        try {
            val remoteData = todoItemsApiService.getAllItems()
            itemDao.insert(remoteData)
            emitAll(itemDao.getAllItems().map { Resource.Success(it) })
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Couldn't fetch todos", data = localData))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTodo(id: Int): Flow<Resource<Item>> = flow {
        emit(Resource.Loading(data = itemDao.getItem(id).firstOrNull()))

        try {
            val response = todoItemsApiService.getItem(id)
            itemDao.update(response)
        } catch (e: Exception) {
            emit(Resource.Error("Failed to fetch todos", data = itemDao.getItem(id).firstOrNull()))
        }
    }.flatMapConcat {
        itemDao.getItem(id).map {
            Resource.Success(it)
        }
    }

}
