package com.example.github_user_search_app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.github_user_search_app.ui.components.AboutSection
import com.example.github_user_search_app.ui.components.ProfileDetailsSection
import com.example.github_user_search_app.ui.components.RepositoriesSection
import com.example.github_user_search_app.viewmodel.UserDetailViewModel
import com.example.github_user_search_app.ui.components.StatsSection
import com.example.github_user_search_app.ui.components.UserInfoSection


val voisGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFE60000),
        Color(0xFFD81B60),
        Color(0xFF7B1FA2)
    )
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    username: String,
    onBackClick: () -> Unit,
    viewModel: UserDetailViewModel = viewModel()
) {
    val state = viewModel.state
    var showDetails by remember { mutableStateOf(false) }
    var showRepositories by remember { mutableStateOf(false) }

    LaunchedEffect(username) {
        viewModel.load(username)
    }

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
                        .background(voisGradient)
                ) {
                    CenterAlignedTopAppBar(
                        title = {
                                Text(
                                    text = "Profile Details",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )

                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                }
            }
        }
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingState(paddingValues)
            state.error != null -> ErrorState(paddingValues, state.error!!)
            state.user != null -> UserContent(
                user = state.user!!,
                showDetails = showDetails,
                onToggleDetails = { showDetails = !showDetails },
                showRepositories = showRepositories,
                onToggleRepositories = { showRepositories = !showRepositories },
                viewModel = viewModel,
                paddingValues = paddingValues
            )
        }
    }
}

@Composable
private fun LoadingState(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp),
                strokeWidth = 6.dp
            )
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Loading profile...",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Please wait while we fetch the user information",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ErrorState(paddingValues: PaddingValues, message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                Icons.Filled.Badge,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(72.dp)
            )
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Oops! Something went wrong",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.error
                )
                
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun UserContent(
    user: com.example.github_user_search_app.data.model.User,
    showDetails: Boolean,
    onToggleDetails: () -> Unit,
    showRepositories: Boolean,
    onToggleRepositories: () -> Unit,
    viewModel: UserDetailViewModel,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF8F9FA)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UserInfoSection(user)
        StatsSection(user)
        if (!user.bio.isNullOrBlank()) {
            AboutSection(user.bio)
        }
        ProfileDetailsSection(user, showDetails, onToggleDetails)
        RepositoriesSection(user, showRepositories, onToggleRepositories, viewModel)
        GitHubButton(user)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun GitHubButton(user: com.example.github_user_search_app.data.model.User) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(user.htmlUrl)))
            }
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0xFFE60000).copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(voisGradient)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Filled.Link,
                    contentDescription = "GitHub",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "View Profile on GitHub",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

