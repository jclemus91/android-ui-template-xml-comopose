package com.jclemus.androidsamples.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jclemus.androidsamples.data.SocialRepository
import com.jclemus.androidsamples.domain.Post
import com.jclemus.androidsamples.domain.SuggestedFriend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class UiState(
    val posts: List<Post> = emptyList(),
    val suggestedFriends: List<SuggestedFriend> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val currentUserId: String = "user1"
)

class SocialFeedViewModel(
    private val repository: SocialRepository = SocialRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            val posts = repository.getPosts()
            val suggestedFriends = repository.getSuggestedFriends(_uiState.value.currentUserId)

            _uiState.update {
                it.copy(
                    posts = posts,
                    suggestedFriends = suggestedFriends,
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }
    }

    fun refresh() {
        _uiState.update {
            it.copy(isRefreshing = true)
        }
        loadFeed()
    }

}