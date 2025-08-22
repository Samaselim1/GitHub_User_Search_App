package com.example.github_user_search_app.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val login: String,
    val id: Long,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val htmlUrl: String,
    val type: String? = null,
    val score: Double? = null,
    @SerializedName("site_admin") val siteAdmin: Boolean? = null,
    // Detail fields (optional in search response)
    val name: String? = null,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val email: String? = null,
    val bio: String? = null,
    @SerializedName("public_repos") val publicRepos: Int? = null,
    @SerializedName("public_gists") val publicGists: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null,
    @SerializedName("twitter_username") val twitterUsername: String? = null,
    val hireable: Boolean? = null,
    @SerializedName("node_id") val nodeId: String? = null,
    @SerializedName("user_view_type") val userViewType: String? = null,
    @SerializedName("followers_url") val followersUrl: String? = null,
    @SerializedName("following_url") val followingUrl: String? = null,
    @SerializedName("gists_url") val gistsUrl: String? = null,
    @SerializedName("starred_url") val starredUrl: String? = null,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String? = null,
    @SerializedName("organizations_url") val organizationsUrl: String? = null,
    @SerializedName("repos_url") val reposUrl: String? = null,
    @SerializedName("events_url") val eventsUrl: String? = null,
    @SerializedName("received_events_url") val receivedEventsUrl: String? = null
)
