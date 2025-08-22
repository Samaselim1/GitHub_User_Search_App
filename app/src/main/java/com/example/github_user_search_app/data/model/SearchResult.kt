package com.example.github_user_search_app.data.model

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    val items: List<User>
)
