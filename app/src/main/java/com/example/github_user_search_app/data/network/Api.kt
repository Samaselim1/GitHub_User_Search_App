package com.example.github_user_search_app.data.network

import com.example.github_user_search_app.data.model.SearchResult
import com.example.github_user_search_app.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): SearchResult

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): User
}