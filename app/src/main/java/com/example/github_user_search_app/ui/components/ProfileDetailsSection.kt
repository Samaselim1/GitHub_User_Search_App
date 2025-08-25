package com.example.github_user_search_app.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ProfileDetailsSection(
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
            actionText = if (showDetails) "Hide" else "Show",
            onActionClick = onToggleDetails
        ) {
            if (showDetails) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    DetailRow(Icons.Filled.Business, "Company", user.company)
                    DetailRow(Icons.Filled.Place, "Location", user.location)
                    DetailRow(Icons.Filled.Email, "Email", user.email, isLink = true)
                    DetailRow(Icons.Filled.AlternateEmail, "Twitter", user.twitterUsername?.let { "@$it" })
                }
            } else {
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
private fun DetailRow(
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
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(value)))
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
            DetailRowIcon(icon, isLink)
            Spacer(Modifier.width(16.dp))
            DetailRowContent(label, value, isLink)
            DetailRowLinkIcon(isLink)
        }
    }
}

@Composable
private fun DetailRowIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isLink: Boolean
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isLink) Color(0xFFE60000) else Color(0xFF666666),
        modifier = Modifier.size(24.dp)
    )
}

@Composable
private fun DetailRowContent(
    label: String,
    value: String,
    isLink: Boolean
) {
    Column(modifier = Modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color(0xFF666666)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            color = if (isLink) Color(0xFFE60000) else Color(0xFF2C2C2C)
        )
    }
}

@Composable
private fun DetailRowLinkIcon(isLink: Boolean) {
    if (isLink) {
        Icon(
            imageVector = Icons.Filled.Link,
            contentDescription = "Open Link",
            tint = Color(0xFFE60000),
            modifier = Modifier.size(20.dp)
        )
    }
}