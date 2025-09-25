package com.jclemus.androidsamples.data

import com.jclemus.androidsamples.domain.Comment
import com.jclemus.androidsamples.domain.Post
import com.jclemus.androidsamples.domain.SuggestedFriend
import com.jclemus.androidsamples.domain.User
import kotlinx.coroutines.delay

class SocialRepository(
    private val socialGraphAlgorithms: SocialGraphAlgorithms = SocialGraphAlgorithms()
) {

    private val users = mapOf(
        "user1" to User(
            id = "user1",
            name = "Cindy L. Stephens",
            username = "cls",
            image = "",
            friends = listOf("user2")
        ),
        "user2" to User(
            id = "user2",
            name = "Homer Simpson",
            username = "ndw",
            image = "",
            friends = listOf("user1", "user3", "user4", "user5")
        ),
        "user3" to User(
            id = "user3",
            name = "Naomi D. White",
            username = "ndw",
            image = "",
            friends = listOf("user2")
        ),
        "user4" to User(
            id = "user4",
            name = "Alondra",
            username = "ndw",
            image = "",
            friends = listOf("user2")
        ),
        "user5" to User(
            id = "user5",
            name = "Elvia",
            username = "ndw",
            image = "",
            friends = listOf("user2", "user5")
        ),
        "user6" to User(
            id = "user6",
            name = "Laura",
            username = "ndw",
            image = "",
            friends = listOf("user5")
        )
    )

    private val posts: List<Post> = listOf<Post>(
        Post(
            id = "post1",
            userId = "user1",
            content = "This is a nice post",
            likes = 1346,
            isLiked = false,
            comments = listOf(
                Comment(
                    id = "comment1",
                    userId = "user1",
                    content = "This is a 1 level comment"
                )
            )
        ),
        Post(
            id = "post2",
            userId = "user1",
            content = "This is a nice post",
            likes = 1346,
            isLiked = false,
            comments = listOf(
                Comment(
                    id = "comment1",
                    userId = "user1",
                    content = "This is a 1 level comment"
                )
            )
        ),
        Post(
            id = "post3",
            userId = "user1",
            content = "This is a nice post",
            likes = 1346,
            isLiked = true,
            comments = listOf(
                Comment(
                    id = "comment1",
                    userId = "user1",
                    content = "This is a 1 level comment"
                )
            )
        ),
        Post(
            id = "post4",
            userId = "user1",
            content = "This is a nice post",
            likes = 1346,
            isLiked = false,
            comments = listOf(
                Comment(
                    id = "comment1",
                    userId = "user1",
                    content = "This is a 1 level comment"
                )
            )
        )
    )

    suspend fun getPosts(): List<Post> {
        delay(1000)
        return posts.sortedByDescending { it.timestamp }
    }

    suspend fun getSuggestedFriends(userId: String): List<SuggestedFriend> {
        delay(1000)
        return socialGraphAlgorithms.findSuggestedFriends(
            currentUserId = userId,
            users = users
        )
    }

    fun getUserById(userId: String): User? {
        return users[userId]
    }
}