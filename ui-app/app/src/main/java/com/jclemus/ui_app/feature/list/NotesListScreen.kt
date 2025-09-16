@file:OptIn(ExperimentalMaterial3Api::class)

package com.jclemus.ui_app.feature.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jclemus.ui_app.data.Note
import com.jclemus.ui_app.feature.NotesViewModel

@Composable
fun NotesListScreen(
    viewModel: NotesViewModel,
    onCreateNote: () -> Unit,
    onEditNote: (id: Long) -> Unit
) {
    val state by viewModel.uiSate.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Notest") }) },
        floatingActionButton = { FloatingActionButton(onClick = onCreateNote) { Text("+") } },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(12.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::searchQuery,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search") },
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            if (state.notes.isEmpty()) {
                Text("Add a note to start", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(

                ) {
                    items(state.notes, key = { it.id }) { note ->
                        NoteRow(note) { onEditNote(note.id) }
                    }
                }

            }
        }
    }
}

@Composable
fun NoteRow(note: Note, onEditNote: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditNote)
    ) {
        Row(
            modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (note.imageUri != null) {
                AsyncImage(
                    model = note.imageUri, contentDescription = null, modifier = Modifier.size(48.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = note.title.ifBlank { "No Title" },
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = note.body,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}