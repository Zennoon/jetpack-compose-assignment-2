package com.mobile.todo.data.repository

import com.mobile.todo.data.local.Item
import com.mobile.todo.data.local.ItemDao
import com.mobile.todo.data.models.Resource
import com.mobile.todo.data.remote.TodoItemsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
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

    override fun getTodo(id: Int): Flow<Resource<Item>> = flow {
        emit(Resource.Loading())

        val localData = itemDao.getItem(id).firstOrNull()
        emit(Resource.Loading(data = localData))

        try {
            val remoteData = todoItemsApiService.getItem(id)
            itemDao.update(remoteData)
            emitAll(itemDao.getItem(id).map { Resource.Success(it) })
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Couldn't fetch todo", data = localData))
        }
    }

}
