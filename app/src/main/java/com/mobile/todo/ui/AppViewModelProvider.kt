package com.mobile.todo.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mobile.todo.TodoApplication
import com.mobile.todo.ui.viewmodel.TodoItemDetailsScreenViewModel
import com.mobile.todo.ui.viewmodel.TodoItemsListScreenViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for TodoItemsListScreenViewModel
        initializer {
            TodoItemsListScreenViewModel(
                todoItemsRepository = todoApplication().container.todoItemsRepository
            )
        }

        // Initializer for TodoItemDetailsViewModel
        initializer {
            TodoItemDetailsScreenViewModel(
                this.createSavedStateHandle(),
                todoItemsRepository = todoApplication().container.todoItemsRepository
            )
        }
    }
}

fun CreationExtras.todoApplication(): TodoApplication = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TodoApplication)
