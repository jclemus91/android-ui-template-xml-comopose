package com.jclemus.androidsamples.domain

data class User(
    val id: Long,
    val name: String,
    val username: String,
    val avatar: String = "",
    val friends: List<Long> = emptyList()
)

data class Post(
    val id: Long,
    val userId: Long,
    val content: String,
    val timestamp: Long,
    val likes: Int,
    val isLiked: Boolean = false,
    val comments: List<Comment> = emptyList(),
    val parentId: Long? = null
)

data class Comment(
    val id: Long,
    val userId: Long,
    val content: String,
    val timestamp: Long,
    val replies: List<Comment> = emptyList()
)

data class SuggestedFriend(
    val user: User,
    val mutualFriends: Int,
    val connectionPath: List<User>
)
