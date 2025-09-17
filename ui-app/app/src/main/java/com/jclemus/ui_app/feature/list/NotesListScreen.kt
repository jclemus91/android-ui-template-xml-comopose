@file:OptIn(ExperimentalMaterial3Api::class)

package com.jclemus.ui_app.feature.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jclemus.ui_app.data.Note
import com.jclemus.ui_app.feature.NotesViewModel

@Composable
fun NotesListScreen(viewModel: NotesViewModel, onCreate: () -> Unit, onEdit: (id: Long) -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Notes ${state.notes.size}") }) },
        floatingActionButton = { FloatingActionButton(onClick = onCreate)  { Text("Add") }}
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize().padding(8.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = {
                    viewModel.search(it)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))
            if (state.notes.isEmpty()) {
                Text("Add a note to start")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(8.dp)
                ) {

                    items(state.notes, key = {it.id}) { note ->
                        NoteRow(note) {
                            onEdit(note.id)
                        }
                        HorizontalDivider()

                    }
                }
            }
        }
    }
}

@Composable
fun NoteRow(note: Note, onEdit: (Long) -> Unit) {
    ElevatedCard(
        onClick = {
            onEdit.invoke(note.id)
        },
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        if (note.imageUri != null) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                AsyncImage(
                    model = note.imageUri,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
                Column(
                    modifier = Modifier.padding(8.dp).weight(1f)
                ) {
                    Text(text = note.title.ifEmpty { "No Title" }, maxLines = 1, style = MaterialTheme.typography.titleMedium)
                    Text(text = note.body, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = note.title.ifEmpty { "No Title" }, maxLines = 1, style = MaterialTheme.typography.titleMedium)
                Text(text = note.body, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
            }
        }

    }
}