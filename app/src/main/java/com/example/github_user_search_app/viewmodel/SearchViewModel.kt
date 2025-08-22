package com.example.github_user_search_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github_user_search_app.data.model.User
import com.example.github_user_search_app.data.network.RetrofitInstance
import com.example.github_user_search_app.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class SearchState(
    val query: String = "",
    val page: Int = 1,
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false
)
class SearchViewModel(
    private val repository: UserRepository = UserRepository(RetrofitInstance.api)
) : ViewModel(){

    var state by mutableStateOf(SearchState())
        private set

    fun onQueryChange(newQuery: String) {
        state = state.copy(query = newQuery)
    }

    fun search(reset: Boolean = false) {
        val q = state.query.trim()
        if(q.isEmpty()) {
            state = state.copy(
                users = emptyList(),
                error = null, 
                endReached = false, 
                page = 1,
                isLoading = false
            )
            return
        }

        if (state.isLoading) return

        viewModelScope.launch {
            try {
                val nextPage = if (reset) 1 else state.page + 1
                state = state.copy(isLoading = true, error = null)

                if (!reset) {
                    delay(300)
                }

                val result = repository.searchUsers(q, nextPage)

                state = result.fold(
                    onSuccess = { resp ->
                        val mergedUsers = if (reset) resp.items else state.users + resp.items
                        state.copy(
                            users = mergedUsers,
                            page = nextPage,
                            endReached = resp.items.size < 30,
                            isLoading = false
                        )
                    },
                    onFailure = { e ->
                        state.copy(
                            error = e.localizedMessage ?: "Unknown error", 
                            isLoading = false
                        )
                    }
                )
            } catch (e: Exception) {
                state = state.copy(
                    error = e.localizedMessage ?: "Unknown error", 
                    isLoading = false
                )
            }
        }
    }
    
    fun clearSearch() {
        state = state.copy(
            query = "",
            users = emptyList(),
            error = null,
            endReached = false,
            page = 1,
            isLoading = false
        )
    }
}
