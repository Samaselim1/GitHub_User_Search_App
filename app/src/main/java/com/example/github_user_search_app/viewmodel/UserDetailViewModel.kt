package com.example.github_user_search_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github_user_search_app.data.model.User
import com.example.github_user_search_app.data.model.Repository
import com.example.github_user_search_app.data.network.RetrofitInstance
import com.example.github_user_search_app.data.repository.UserRepository
import kotlinx.coroutines.launch

data class UserDetailState(
    val user: User? = null,
    val repositories: List<Repository> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingRepositories: Boolean = false,
    val isLoadingMoreRepositories: Boolean = false,
    val currentPage: Int = 1,
    val hasMoreRepositories: Boolean = true,
    val error: String? = null
)

class UserDetailViewModel(
    private val repository: UserRepository = UserRepository(RetrofitInstance.api)
) : ViewModel() {

    var state by mutableStateOf(UserDetailState())
        private set

    fun load(username: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val result = repository.getUserDetail(username)
            state = result.fold(
                onSuccess = { detail -> state.copy(user = detail, isLoading = false) },
                onFailure = { e -> state.copy(error = e.localizedMessage ?: "Unknown error", isLoading = false) }
            )
        }
    }

    fun loadRepositories(username: String) {
        viewModelScope.launch {
            state = state.copy(isLoadingRepositories = true, currentPage = 1)
            val result = repository.getUserRepositories(username, page = 1)
            state = result.fold(
                onSuccess = { repos ->
                    state.copy(
                        repositories = repos,
                        isLoadingRepositories = false,
                        hasMoreRepositories = repos.size >= 5,
                        currentPage = 1
                    )
                },
                onFailure = { e ->
                    state.copy(
                        error = e.localizedMessage ?: "Unknown error",
                        isLoadingRepositories = false
                    )
                }
            )
        }
    }

    fun loadMoreRepositories(username: String) {
        if (state.isLoadingMoreRepositories || !state.hasMoreRepositories) return

        viewModelScope.launch {
            state = state.copy(isLoadingMoreRepositories = true)
            val nextPage = state.currentPage + 1
            val result = repository.getUserRepositories(username, page = nextPage)
            state = result.fold(
                onSuccess = { repos ->
                    val updatedRepositories = state.repositories + repos
                    state.copy(
                        repositories = updatedRepositories,
                        isLoadingMoreRepositories = false,
                        hasMoreRepositories = repos.size >= 5,
                        currentPage = nextPage
                    )
                },
                onFailure = { e ->
                    state.copy(
                        error = e.localizedMessage ?: "Unknown error",
                        isLoadingMoreRepositories = false
                    )
                }
            )
        }
    }
} 