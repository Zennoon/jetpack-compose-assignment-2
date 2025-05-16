package com.mobile.todo.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mobile.todo.TodoApplication
import com.mobile.todo.ui.viewmodel.TodoItemsListScreenViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TodoItemsListScreenViewModel(
                todoItemsRepository = todoApplication().container.todoItemsRepository
            )
        }
    }
}

fun CreationExtras.todoApplication(): TodoApplication = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TodoApplication)
