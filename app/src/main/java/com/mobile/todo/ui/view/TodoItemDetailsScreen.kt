package com.mobile.todo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.mobile.todo.ui.viewmodel.TodoItemDetailsScreenViewModel

@Composable
fun TodoItemDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoItemDetailsScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
                    TodoItemDetailsCard(it)
                }
            }
        }

        is Resource.Success -> {
            TodoItemDetailsCard(
                item = (uiState as Resource.Success).data,
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
                    Button(onClick = { viewModel.fetchTodo() }) {
                        Text("Retry")
                    }
                }
                (uiState as Resource.Error).data?.let {
                    TodoItemDetailsCard(it)
                }
            }
        }
    }
}

@Composable
fun TodoItemDetailsCard(
    item: Item,
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
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(images[item.id % 3]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(60.dp)
                        .width(60.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(item.title, style = MaterialTheme.typography.titleLarge)
                    CompletedChip(completed = item.completed)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.lorem_long),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun CompletedChip(
    completed: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        shape = RoundedCornerShape(topStartPercent = 35, bottomEndPercent = 35),
        modifier = modifier
    ) {
       Text(
           stringResource(if (completed) R.string.completed else R.string.not_completed),
           style = MaterialTheme.typography.labelLarge,
           modifier = Modifier
               .padding(8.dp)
       )
    }
}
