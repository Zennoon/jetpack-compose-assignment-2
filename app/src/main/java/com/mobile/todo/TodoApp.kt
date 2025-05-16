package com.mobile.todo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mobile.todo.ui.view.TodoItemsListScreen

sealed class TodoAppScreen(val route: String, val title: String) {
    data object TodoItemsListScreen: TodoAppScreen(route = "todoItems", title = "Todo Items")
    data object TodoItemDetails: TodoAppScreen(route = "todoItemDetails/{id}", title = "Todo Item Details") {
        fun createRoute(id: String) = "todoItemDetails/$id"
    }
}

fun getScreenFromRoute(route: String?): TodoAppScreen {
    return when {
        route == TodoAppScreen.TodoItemsListScreen.route -> TodoAppScreen.TodoItemsListScreen
        (route?.startsWith("todoItemDetails") ?: false) -> TodoAppScreen.TodoItemDetails
        else -> TodoAppScreen.TodoItemsListScreen
    }
}

@Composable
fun TodoApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = getScreenFromRoute(backStackEntry?.destination?.route)

    Scaffold(
        topBar = {
            TodoAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TodoAppScreen.TodoItemsListScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = TodoAppScreen.TodoItemsListScreen.route) {
                TodoItemsListScreen(
                    onItemClick = {
                        navController.navigate(TodoAppScreen.TodoItemDetails.createRoute(it.toString()))
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoAppBar(
    currentScreen: TodoAppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(currentScreen.title, style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}
