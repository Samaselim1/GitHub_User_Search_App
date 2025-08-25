package com.example.github_user_search_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun UserInfoSection(user: com.example.github_user_search_app.data.model.User) {
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ProfileAvatar(user.avatarUrl)
            UserInfo(user)
        }
    }
}

@Composable
fun ProfileAvatar(avatarUrl: String) {
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
            model = avatarUrl,
            contentDescription = "Profile picture",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun UserInfo(user: com.example.github_user_search_app.data.model.User) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = user.name ?: user.login,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF2C2C2C)
        )

        if (user.name != null) {
            Text(
                text = "@${user.login}",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFE60000)
            )
        }
        if (user.createdAt != null) {
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

@Composable
fun StatsSection(user: com.example.github_user_search_app.data.model.User) {
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
            StatItem(Icons.Filled.Star, "${user.publicRepos ?: 0}", "Repositories")
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = Color(0xFFE60000).copy(alpha = 0.2f)
            )
            StatItem(Icons.Filled.People, "${user.followers ?: 0}", "Followers")
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = Color(0xFFE60000).copy(alpha = 0.2f)
            )
            StatItem(Icons.Filled.PersonAdd, "${user.following ?: 0}", "Following")
        }
    }
}


@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFFE60000),
            modifier = Modifier.size(28.dp)
        )

        Text(
            text = count,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF2C2C2C)
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Color(0xFF666666)
        )
    }
}


@Composable
fun AboutSection(bio: String) {
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