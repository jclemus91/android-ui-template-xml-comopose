package com.jclemus.androidsamples.data

import com.jclemus.androidsamples.domain.SuggestedFriend
import com.jclemus.androidsamples.domain.User
import java.util.ArrayDeque
import java.util.Queue

class SocialGraphAlgorithms() {

    fun findSuggestedFriends(
        currentUserId: String,
        users: Map<String, User>
    ) : List<SuggestedFriend> {

        val currentUser = users[currentUserId] ?: return emptyList()
        val visited = mutableSetOf<String>()
        val queue: Queue<Pair<String, List<User>>> = ArrayDeque()
        val suggestions = mutableListOf<SuggestedFriend>()

        visited.add(currentUserId)

        // Add direct friends to visited we don't suggest existing friends
        currentUser.friends.forEach { friendId ->
            visited.add(friendId)
            users[friendId]?.let { friend ->
                queue.offer(friendId to listOf(currentUser, friend))
            }
        }

        // BFS to find friends of friends
        while(queue.isNotEmpty()) {
            val (userId, path) = queue.poll()
            val user = users[userId] ?: continue

            user.friends.forEach { friendId ->
                if (!visited.contains(friendId)) {
                    visited.add(friendId)
                    users[friendId]?.let { potentialFriend ->

                        val newPath = path + potentialFriend

                        // Calculate mutual friends
                        val mutualCount = currentUser.friends.intersect(potentialFriend.friends.toSet()).size

                        suggestions.add(
                            SuggestedFriend(
                                user = potentialFriend,
                                mutualFriends = mutualCount,
                                connectionPath = newPath
                            )
                        )

                        // Continue BFS for deeper connections
                        if (path.size < 3) {
                            queue.offer(friendId to newPath)
                        }
                    }
                }
            }
        }
        return suggestions.sortedByDescending { it.mutualFriends }
    }
}