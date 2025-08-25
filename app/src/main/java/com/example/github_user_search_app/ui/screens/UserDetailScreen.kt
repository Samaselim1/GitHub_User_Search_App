
package com.example.github_user_search_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.github_user_search_app.viewmodel.UserDetailViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    username: String,
    onBackClick: () -> Unit,
    viewModel: UserDetailViewModel = viewModel()
) {
    val state = viewModel.state
    var showDetails by remember { mutableStateOf(false) }

    val voisGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFE60000), // VOIS red
            Color(0xFFD81B60), // pink accent
            Color(0xFF7B1FA2)  // purple accent
        )
    )

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
                paddingValues = paddingValues
            )
        }
    }
}

@Composable
private fun LoadingState(paddingValues: PaddingValues) {
    Box(
        Modifier.fillMaxSize().padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp),
                strokeWidth = 6.dp
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Loading profile...",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Please wait while we fetch the user information",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ErrorState(paddingValues: PaddingValues, message: String) {
    Box(
        Modifier.fillMaxSize().padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Badge,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(72.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Oops! Something went wrong",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun UserContent(
    user: com.example.github_user_search_app.data.model.User,
    showDetails: Boolean,
    onToggleDetails: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF8F9FA))
    ) {
        HeroSection(user)
        
        // Stats Section
        StatsSection(user)
        
        // About Section
        if (!user.bio.isNullOrBlank()) {
            AboutSection(user.bio)
        }

        // Profile Details
        ProfileDetailsSection(user, showDetails, onToggleDetails)

        GitHubLinksSection(user)

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun HeroSection(user: com.example.github_user_search_app.data.model.User) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = CircleShape,
                        spotColor = Color(0xFFE60000).copy(alpha = 0.4f)
                    )
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "Profile picture",
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Name and Username
            Text(
                text = user.name ?: user.login,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF2C2C2C)
            )
            
            if (user.name != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "@${user.login}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFE60000)
                )
            }
            
            // Join date if available
            if (user.createdAt != null) {
                Spacer(Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.CalendarToday,
                        contentDescription = "Joined",
                        tint = Color(0xFF666666),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Joined ${user.createdAt}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsSection(user: com.example.github_user_search_app.data.model.User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0xFFE60000).copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EnhancedStatItem(Icons.Filled.Star, "${user.publicRepos ?: 0}", "Repositories")
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = Color(0xFFE60000).copy(alpha = 0.2f)
            )
            EnhancedStatItem(Icons.Filled.People, "${user.followers ?: 0}", "Followers")
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = Color(0xFFE60000).copy(alpha = 0.2f)
            )
            EnhancedStatItem(Icons.Filled.PersonAdd, "${user.following ?: 0}", "Following")
        }
    }
}

@Composable
private fun AboutSection(bio: String) {
    SectionCard(
        title = "About",
        icon = Icons.Filled.Visibility
    ) {
        Text(
            text = bio,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2f
        )
    }
}

@Composable
private fun ProfileDetailsSection(
    user: com.example.github_user_search_app.data.model.User,
    showDetails: Boolean,
    onToggleDetails: () -> Unit
) {
    val hasProfileDetails = !user.company.isNullOrBlank() ||
                           !user.location.isNullOrBlank() || 
                           !user.email.isNullOrBlank() ||
                           !user.twitterUsername.isNullOrBlank()
    
    if (hasProfileDetails) {
        SectionCard(
            title = "Profile Details",
            icon = Icons.Filled.Badge,
            actionText = if (showDetails) "Hide Details" else "Show Details",
            onActionClick = onToggleDetails
        ) {
            if (showDetails) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    EnhancedDetailRow(Icons.Filled.Business, "Company", user.company)
                    EnhancedDetailRow(Icons.Filled.Place, "Location", user.location)
                    EnhancedDetailRow(Icons.Filled.Email, "Email", user.email, isLink = true)
                    EnhancedDetailRow(Icons.Filled.AlternateEmail, "Twitter", user.twitterUsername?.let { "@$it" })
                }
            } else {
                Text(
                    text = "Tap to view detailed profile information",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        SectionCard(
            title = "Profile Details",
            icon = Icons.Filled.Badge
        ) {
            Text(
                text = "No Profile Details Available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GitHubLinksSection(user: com.example.github_user_search_app.data.model.User) {
    SectionCard(
        title = "GitHub Links",
        icon = Icons.Filled.Link
    ) {
        val context = LocalContext.current
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EnhancedLinkItem(Icons.Filled.Star, "Repositories", "${user.publicRepos ?: 0} repositories") {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(user.reposUrl)))
            }
            EnhancedLinkItem(Icons.Filled.People, "Followers", "${user.followers ?: 0} followers") {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(user.followersUrl)))
            }
            EnhancedLinkItem(Icons.Filled.PersonAdd, "Following", "${user.following ?: 0} following") {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(user.htmlUrl)))
            }
            EnhancedLinkItem(Icons.Filled.Link, "View on GitHub", "github.com/${user.login}") {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(user.htmlUrl)))
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0xFFE60000).copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color(0xFFE60000),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF2C2C2C)
                    )
                }
                if (actionText != null && onActionClick != null) {
                    TextButton(
                        onClick = onActionClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFFE60000)
                        )
                    ) {
                        Text(
                            actionText,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            content()
        }
    }
}

@Composable
private fun EnhancedStatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFFE60000),
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = count,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF2C2C2C)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = Color(0xFF666666)
        )
    }
}

@Composable
private fun EnhancedDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String?,
    isLink: Boolean = false
) {
    if (!value.isNullOrBlank()) {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = isLink) {
                    if (isLink) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
                        context.startActivity(intent)
                    }
                }
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = if (isLink) Color(0xFFE60000).copy(alpha = 0.1f)
                    else Color.Transparent
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isLink) Color(0xFFE60000) else Color(0xFF666666),
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF666666)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = if (isLink) Color(0xFFE60000) else Color(0xFF2C2C2C)
                )
            }
            if (isLink) {
                Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = "Open Link",
                    tint = Color(0xFFE60000),
                    modifier = Modifier.size(20.dp)
                    )
            }
        }
    }
}

@Composable
private fun EnhancedLinkItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFFE60000),
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF2C2C2C)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666)
                )
            }
            Icon(
                imageVector = Icons.Filled.Link,
                contentDescription = "Open Link",
                tint = Color(0xFFE60000),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


