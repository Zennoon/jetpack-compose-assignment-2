package com.mobile.todo.data.repository

import com.mobile.todo.data.local.Item
import com.mobile.todo.data.local.ItemDao
import com.mobile.todo.data.models.Resource
import com.mobile.todo.data.remote.TodoItemsApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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
        emit(Resource.Loading(data = itemDao.getAllItems().firstOrNull()))

        try {
            val response = todoItemsApiService.getAllItems()
            itemDao.insert(response)
        } catch (e: Exception) {
            emit(Resource.Error("Failed to fetch todos", data = itemDao.getAllItems().firstOrNull()))
        }
    }.flatMapConcat {
        itemDao.getAllItems().map {
            Resource.Success(it)
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
