package com.example.github_user_search_app.data.repository

import com.example.github_user_search_app.data.model.SearchResult
import com.example.github_user_search_app.data.model.User
import com.example.github_user_search_app.data.network.Api

class UserRepository(private val api: Api) {

    suspend fun searchUsers(query: String, page: Int=1): Result<SearchResult> {
        return try {
            val response = api.search(query=query, page=page)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserDetail(username: String): Result<User> {
        return try {
            val response = api.getUserDetail(username)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}