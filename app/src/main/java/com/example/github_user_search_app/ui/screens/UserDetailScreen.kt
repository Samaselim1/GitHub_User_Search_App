package com.example.github_user_search_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.github_user_search_app.viewmodel.UserDetailViewModel
import android.content.Intent
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    username: String,
    onBackClick: () -> Unit,
    viewModel: UserDetailViewModel = viewModel()
) {
    val state = viewModel.state

    LaunchedEffect(username) {
        viewModel.load(username)
    }

    Scaffold(
        topBar = {
            Surface {
                Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(username, style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${state.error}", color = MaterialTheme.colorScheme.error)
                }
            }
            state.user != null -> {
                val user = state.user
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = user.avatarUrl,
                            contentDescription = null,
                            modifier = Modifier.size(84.dp).clip(CircleShape)
                        )
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = user.name ?: user.login, style = MaterialTheme.typography.headlineSmall)
                            if (user.name != null) {
                                Text(text = "@${user.login}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    if (!user.bio.isNullOrBlank()) {
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                            Text(text = user.bio, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(16.dp))
                        }
                        Spacer(Modifier.height(12.dp))
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Stat(Icons.Filled.Star, (user.publicRepos ?: 0).toString(), "Repos")
                        Stat(Icons.Filled.People, (user.followers ?: 0).toString(), "Followers")
                        Stat(Icons.Filled.PersonAdd, (user.following ?: 0).toString(), "Following")
                    }

                    Spacer(Modifier.height(16.dp))

                    // Details list
                    DetailRow(icon = Icons.Filled.Business, label = "Company", value = user.company)
                    DetailRow(icon = Icons.Filled.Place, label = "Location", value = user.location)
                    DetailRow(icon = Icons.Filled.Link, label = "Blog", value = user.blog)
                    DetailRow(icon = Icons.Filled.Email, label = "Email", value = user.email)
                    DetailRow(icon = Icons.Filled.Badge, label = "Type", value = user.type)
                    DetailRow(icon = Icons.Filled.AlternateEmail, label = "Twitter", value = user.twitterUsername?.let { "@$it" })
                    DetailRow(icon = Icons.Filled.DateRange, label = "Created", value = user.createdAt)
                    DetailRow(icon = Icons.Filled.DateRange, label = "Updated", value = user.updatedAt)
                    DetailRow(icon = Icons.Filled.Badge, label = "Hireable", value = user.hireable?.let { if (it) "Yes" else "No" })
                    DetailRow(icon = Icons.Filled.Badge, label = "Node ID", value = user.nodeId)
                    DetailRow(icon = Icons.Filled.Badge, label = "View Type", value = user.userViewType)

                    Spacer(Modifier.height(16.dp))

                    // GitHub Links Section
                    Text(
                        text = "GitHub Links",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    val context = LocalContext.current
                    
                    LinkRow(
                        icon = Icons.Filled.Star,
                        label = "Repositories",
                        value = "${user.publicRepos ?: 0} repos",
                        onClick = { 
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.reposUrl))
                            context.startActivity(intent)
                        }
                    )
                    
                    LinkRow(
                        icon = Icons.Filled.People,
                        label = "Followers",
                        value = "${user.followers ?: 0} followers",
                        onClick = { 
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.followersUrl))
                            context.startActivity(intent)
                        }
                    )
                    
                    LinkRow(
                        icon = Icons.Filled.PersonAdd,
                        label = "Following",
                        value = "${user.following ?: 0} following",
                        onClick = { 
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.followingUrl))
                            context.startActivity(intent)
                        }
                    )
                    
                    LinkRow(
                        icon = Icons.Filled.Badge,
                        label = "Gists",
                        value = "${user.publicGists ?: 0} gists",
                        onClick = { 
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.gistsUrl))
                            context.startActivity(intent)
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(onClick = { 
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.htmlUrl))
                        context.startActivity(intent)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Filled.Link, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Open GitHub Profile")
                    }
                }
            }
        }
    }
}

@Composable
private fun Stat(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null)
        Text(text = value, style = MaterialTheme.typography.titleMedium)
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Column {
                Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = value, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun LinkRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
        Icon(
            Icons.Filled.Link,
            contentDescription = "Open link",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
    }
} 