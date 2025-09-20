package com.jclemus.ui_app.feature.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jclemus.ui_app.feature.ExploreViewModel

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel,
    onGoToDetail: (Long, String) -> Unit
) {

    val uiState by viewModel.state.collectAsState()

    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = {
                    viewModel.setQuery(it)
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                placeholder = { Text("Search") }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(uiState.sections, key = { it.id }) { section ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(section.title)

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(8.dp),
                        ) {
                            items(items = section.homes, key = { it.id }) { home ->
                                OutlinedCard {
                                    Column(
                                        modifier = Modifier.clickable{
                                            onGoToDetail.invoke(home.id, home.title)
                                        }
                                    ) {
                                        Box(
                                            modifier = Modifier.background(Color.Cyan)
                                        )
                                        Text(home.title, style = MaterialTheme.typography.titleMedium)
                                        Text(home.price + home.description, style = MaterialTheme.typography.bodyMedium)
                                        Text("* ${home.rating}", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}