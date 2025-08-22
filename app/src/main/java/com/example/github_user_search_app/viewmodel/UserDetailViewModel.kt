package com.example.github_user_search_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github_user_search_app.data.model.User
import com.example.github_user_search_app.data.network.RetrofitInstance
import com.example.github_user_search_app.data.repository.UserRepository
import kotlinx.coroutines.launch

data class UserDetailState(
    val user: User? = null,
    val isLoading: Boolean = false,
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
} 