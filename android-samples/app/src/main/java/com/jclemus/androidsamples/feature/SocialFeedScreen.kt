package com.jclemus.androidsamples.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jclemus.androidsamples.domain.Post
import com.jclemus.androidsamples.domain.SuggestedFriend
import kotlin.collections.forEach


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialFeedScreen(
    viewModel: SocialFeedViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Feed")
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.refresh()
                        }
                    ) {
                        Icon(
                            Icons.Default.Refresh, contentDescription = null
                        )
                    }
                }
            )

        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.posts.isEmpty() -> {
                    LoadingContent()
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        if (uiState.isRefreshing) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text("Refreshing...")
                                    }
                                }
                            }
                        }

                        if (uiState.suggestedFriends.isNotEmpty()) {
                            item {
                                SuggestionFriendsSection(
                                    suggestedFriends = uiState.suggestedFriends
                                )
                            }
                        }

                        val posts = uiState.posts
                        items(items = posts, key = { it.id }) { post ->
                            PostCard(post)
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading your feed...")

        }
    }
}


@Composable
fun SuggestionFriendsSection(
    suggestedFriends: List<SuggestedFriend>
) {

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null
                )

                Spacer(Modifier.width(12.dp))

                Text("People you may know")

                Spacer(Modifier.width(8.dp))
            }

            Spacer(Modifier.height(12.dp))

            suggestedFriends.forEach { suggestedFriend ->
                SuggestionFriendSection(
                    suggestedFriend
                )
                if (suggestedFriend != suggestedFriends.last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

}

@Composable
fun SuggestionFriendSection(
    suggestedFriend: SuggestedFriend
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Card(
            modifier = Modifier.size(40.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),

                ) {
                Text(text = suggestedFriend.user.name.first().toString())
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = suggestedFriend.user.name
            )

            if (suggestedFriend.mutualFriends > 0) {
                Text(
                    text = "${suggestedFriend.mutualFriends} mutual friends"
                )
            }


            if (suggestedFriend.connectionPath.size > 2) {
                val pathText = suggestedFriend.connectionPath.drop(1).dropLast(1).joinToString(" -> ") {
                    it.name.split(" ").first()
                }

                Text(text = "via $pathText")
            }

            TextButton(
                onClick = {

                }
            ) {
                Text("Add")
            }
        }

    }
}

@Composable
fun PostCard(post: Post) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = post.userId
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = post.content
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Default.ThumbUp,
                        contentDescription = "like"
                    )
                }
                Text("Likes ${post.likes}")
                Text("Comments ${post.comments.size}")
            }
        }
    }
}