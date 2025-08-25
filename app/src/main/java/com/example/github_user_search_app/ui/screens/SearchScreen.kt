
package com.example.github_user_search_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.github_user_search_app.ui.components.SearchBar
import com.example.github_user_search_app.ui.components.UsersList
import com.example.github_user_search_app.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onUserClick: (String) -> Unit = {}
) {
    val state = viewModel.state

    val voisGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFFE60000),
            Color(0xFFD81B60),
            Color(0xFF7B1FA2)
        )
    )

    Scaffold(
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                shadowElevation = 6.dp,
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(voisGradient),
                    contentAlignment = Alignment.Center
                ) {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "GitHub User Search",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 24.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = Color.White
                        )
                    )

                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SearchBar(
                query = state.query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = { viewModel.search(reset = true) },
                onClear = { viewModel.clearSearch() }
            )

            Spacer(Modifier.height(16.dp))

            if (state.users.isNotEmpty()) {
                Surface(
//                    color = Color(0xFFE60000).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp),
                    tonalElevation = 0.dp
                ) {
                    Text(
                        text = "Found ${state.users.size} user(s)",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE60000)
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            when {
                state.isLoading && state.users.isEmpty() -> {
                    LoadingState()
                }

                state.error != null -> {
                    ErrorState(message = state.error ?: "Unknown error")
                }

                // No Results after Search
                state.users.isEmpty() && state.query.isNotEmpty() -> {
                    EmptyResultsState()
                }

                state.users.isNotEmpty() -> {
                    val listState = rememberLazyListState()
                    LaunchedEffect(listState) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                            .collect { lastIndex ->
                                if (lastIndex != null &&
                                    lastIndex >= state.users.size - 3 &&
                                    !state.isLoading &&
                                    !state.endReached
                                ) {
                                    viewModel.search(reset = false)
                                }
                            }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp) // Increased spacing between items
                    ) {
                        items(state.users, key = { it.id }) { user ->
                            UsersList(
                                user = user,
                                onClick = { onUserClick(user.login) }
                            )
                        }

                        item {
                            Spacer(Modifier.height(16.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    state.isLoading -> {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp), // Slightly larger indicator
                                                strokeWidth = 2.dp
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                "Loading more users...",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            )
                                        }
                                    }

                                    state.endReached -> {
                                        Surface(
                                            color = Color(0xFFE60000).copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(12.dp),
                                            tonalElevation = 0.dp
                                        ) {
                                            Text(
                                                "No more users",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color(0xFFE60000)
                                                ),
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }

                // Default Initial State
                else -> {
                    InitialState(voisGradient)
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(strokeWidth = 3.dp) // Thicker stroke
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Searching users...",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Error,
                contentDescription = null,
                modifier = Modifier.size(64.dp), // Larger icon
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(16.dp)) // Increased spacing
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error
                )
            )
            Spacer(Modifier.height(8.dp)) // Increased spacing
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EmptyResultsState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.size(80.dp), // Even larger icon
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp)) // Increased spacing
            Text(
                text = "No users found",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(Modifier.height(8.dp)) // Increased spacing
            Text(
                text = "Try searching for a different username.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun InitialState(gradient: Brush) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.size(80.dp), // Even larger icon
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(24.dp)) // Increased spacing
            Text(
                text = "Search for GitHub Users",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(Modifier.height(8.dp)) // Increased spacing
            Text(
                text = "Enter a username and tap search",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                )
            )
        }
    }
}


