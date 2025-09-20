package com.jclemus.ui_app.feature.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jclemus.ui_app.feature.ExploreViewModel

@Composable
fun DetailScreen(viewModel: ExploreViewModel, id: Long, title: String) {

    val home = remember { viewModel.getHome(id) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {

            if (home == null) {
                Box(
                ) {
                    Text("SWW")
                }
            } else {
                Column(
                ) {
                    Text(home.title, style = MaterialTheme.typography.titleMedium)
                    Text(home.price + home.description, style = MaterialTheme.typography.bodyMedium)
                    Text("* ${home.rating}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}