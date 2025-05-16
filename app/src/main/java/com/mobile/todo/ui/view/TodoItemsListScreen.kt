package com.mobile.todo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.todo.R
import com.mobile.todo.data.local.Item
import com.mobile.todo.data.models.Resource
import com.mobile.todo.ui.AppViewModelProvider
import com.mobile.todo.ui.Indicator
import com.mobile.todo.ui.viewmodel.TodoItemsListScreenViewModel

@Composable
fun TodoItemsListScreen(
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TodoItemsListScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.todoState.collectAsState()

    when (uiState) {
        is Resource.Loading -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {
                Indicator(size = 32.dp)
                (uiState as? Resource.Loading)?.data?.let {
                    TodoList(it, onItemClick = onItemClick)
                }
            }
        }

        is Resource.Success -> {
            TodoList(
                items = (uiState as Resource.Success).data,
                onItemClick = onItemClick,
                modifier = modifier
            )
        }

        is Resource.Error -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        "Error: ${(uiState as Resource.Error).message}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { viewModel.fetchTodos() }) {
                        Text("Retry")
                    }
                }
                (uiState as Resource.Error).data?.let {
                    TodoList(it, onItemClick = onItemClick)
                }
            }
        }
    }
}

@Composable
fun TodoList(
    items: List<Item>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (items.isEmpty()) {
            Text(
                stringResource(R.string.no_todos),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(items) { item ->
                TodoItemCard(
                    item = item, onClick = onItemClick, modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun TodoItemCard(
    item: Item,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val images = listOf(
        R.drawable.default_img_1,
        R.drawable.default_img_2,
        R.drawable.default_img_3
    )
    Card(
        shape = RoundedCornerShape(topStartPercent = 35, bottomEndPercent = 35),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            12.dp,
            16.dp
        ),
        modifier = modifier
            .clickable { onClick(item.id) }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 24.dp)
        ) {
            Checkbox(
                checked = item.completed,
                enabled = false,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    disabledUncheckedColor = MaterialTheme.colorScheme.primary,
                    disabledCheckedColor = MaterialTheme.colorScheme.primary
                )
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(item.title, style = MaterialTheme.typography.bodyLarge)
                Text(stringResource(R.string.lorem_10), style = MaterialTheme.typography.labelLarge)
            }

            Image(
                painter = painterResource(images[item.id % 3]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .height(60.dp)
                    .width(60.dp)
            )
        }
    }
}
