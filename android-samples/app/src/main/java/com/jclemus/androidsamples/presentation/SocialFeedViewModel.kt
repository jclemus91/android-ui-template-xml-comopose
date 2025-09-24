package com.jclemus.androidsamples.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jclemus.androidsamples.domain.Comment
import com.jclemus.androidsamples.domain.Post
import com.jclemus.androidsamples.domain.SuggestedFriend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val posts: List<Post> = emptyList(),
    val suggestedFriends: List<SuggestedFriend> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val currentUserId: Long = 1L
)

class SocialFeedViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    posts = listOf(
                        Post(
                            id = 1,
                            userId = 1,
                            content = "This is a post",
                            timestamp = System.currentTimeMillis(),
                            likes = 34,
                            isLiked = true,
                            comments = listOf(
                                Comment(
                                    id = 1,
                                    userId = 1,
                                    content = "this is a comment",
                                    timestamp = System.currentTimeMillis(),
                                    replies = listOf(
                                        Comment(
                                            id = 2,
                                            userId = 1,
                                            content = "This is a reply",
                                            timestamp = System.currentTimeMillis(),
                                            replies = emptyList()
                                        )
                                    )
                                )
                            ),
                            parentId = null
                        )
                    )
                )
            }
        }
    }
}