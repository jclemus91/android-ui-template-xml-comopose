package com.jclemus.androidsamples.domain

data class Post(
    val id: String,
    val userId: String,
    val content: String,
    val likes: Int,
    val isLiked: Boolean,
    val comments: List<Comment> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class Comment(
    val id: String,
    val userId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val replies: List<Comment> = emptyList()
)

data class User(
    val id: String,
    val name: String,
    val username: String,
    val image: String,
    val friends: List<String> = emptyList()
)

data class SuggestedFriend(
    val user: User,
    val mutualFriends: Int,
    val connectionPath: List<User>
)