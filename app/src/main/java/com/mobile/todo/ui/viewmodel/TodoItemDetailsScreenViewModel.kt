package com.mobile.todo.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.todo.data.local.Item
import com.mobile.todo.data.models.Resource
import com.mobile.todo.data.repository.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TodoItemDetailsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val todoItemsRepository: TodoItemsRepository
): ViewModel() {
    private val id: Int = checkNotNull(savedStateHandle["id"])
    private val _todoState = MutableStateFlow<Resource<Item>>(Resource.Loading())
    val todoState: StateFlow<Resource<Item>> = _todoState.asStateFlow()

    init {
        fetchTodo()
    }

    fun fetchTodo() {
        todoItemsRepository.getTodo(id)
            .onEach { result ->
                _todoState.value = result
            }
            .catch { _ ->
                _todoState.value = Resource.Error("Unexpected error", data = null)
            }
            .launchIn(viewModelScope)
    }
}