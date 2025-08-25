package com.example.github_user_search_app.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ForkRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.github_user_search_app.viewmodel.UserDetailViewModel

@Composable
fun RepositoriesSection(
    user: com.example.github_user_search_app.data.model.User,
    showRepositories: Boolean,
    onToggleRepositories: () -> Unit,
    viewModel: UserDetailViewModel
) {
    val state = viewModel.state

    LaunchedEffect(showRepositories) {
        if (showRepositories && state.repositories.isEmpty()) {
            viewModel.loadRepositories(user.login)
        }
    }

    SectionCard(
        title = "Repositories",
        icon = Icons.Filled.Star,
        actionText = if (showRepositories) "Hide" else "Show",
        onActionClick = onToggleRepositories
    ) {
        if (showRepositories) {
            when {
                state.isLoadingRepositories -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFE60000),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                state.repositories.isNotEmpty() -> {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Showing ${state.repositories.size} of ${user.publicRepos ?: 0} repositories",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF666666)
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        state.repositories.forEach { repository ->
                            RepositoryItem(repository = repository)
                        }

                        if (state.hasMoreRepositories) {
                            Spacer(Modifier.height(8.dp))
                            ShowMoreButton(
                                isLoading = state.isLoadingMoreRepositories,
                                onClick = { viewModel.loadMoreRepositories(user.login) }
                            )
                        }
                    }
                }
                else -> {
                    Text(
                        text = "No repositories found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RepositoryItem(repository: com.example.github_user_search_app.data.model.Repository) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(repository.htmlUrl)))
            }
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color(0xFFE60000).copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RepositoryHeader(repository)
            RepositoryDescription(repository.description)
            RepositoryStats(repository)
            RepositoryLastUpdated(repository.updatedAt)
        }
    }
}


@Composable
private fun RepositoryHeader(repository: com.example.github_user_search_app.data.model.Repository) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = repository.name,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF2C2C2C),
            modifier = Modifier.weight(1f)
        )

        repository.language?.let { language ->
            LanguageBadge(language)
        }
    }
}

@Composable
private fun LanguageBadge(language: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE60000).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = language,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Color(0xFFE60000),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun RepositoryDescription(description: String?) {
    if (!description.isNullOrBlank()) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF666666),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun RepositoryStats(repository: com.example.github_user_search_app.data.model.Repository) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RepositoryStatItem(
            icon = Icons.Filled.Star,
            count = repository.stargazersCount.toString(),
            label = "Stars"
        )
        RepositoryStatItem(
            icon = Icons.Filled.ForkRight,
            count = repository.forksCount.toString(),
            label = "Forks"
        )
        RepositoryStatItem(
            icon = Icons.Filled.Visibility,
            count = repository.watchersCount.toString(),
            label = "Watchers"
        )
    }
}

@Composable
private fun RepositoryStatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: String,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFFE60000),
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = count,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF666666)
        )
    }
}

@Composable
private fun RepositoryLastUpdated(updatedAt: String) {
    Text(
        text = "Updated $updatedAt",
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF999999)
    )
}

@Composable
private fun ShowMoreButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isLoading, onClick = onClick)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color(0xFFE60000).copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFE60000),
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Loading more repositories...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFF666666)
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.ExpandMore,
                        contentDescription = "Show More",
                        tint = Color(0xFFE60000),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Show More Repositories",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color(0xFFE60000)
                    )
                }
            }
        }
    }
}
