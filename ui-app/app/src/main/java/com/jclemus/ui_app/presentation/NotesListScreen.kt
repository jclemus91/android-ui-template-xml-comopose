package com.jclemus.ui_app.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun NotesListScreen(viewModel: NotesViewModel, onAdd: () -> Unit, onEdit: (id: Long) -> Unit) {

    val notes by viewModel.notes.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            items(notes, key = { it.id}) { note ->
                ListItem(
                    headlineContent = {
                        Text(note.title, style = MaterialTheme.typography.titleMedium)
                    },
                    supportingContent = {
                        Text(note.body, style = MaterialTheme.typography.bodyMedium)
                    },
                    leadingContent = {

                    },
                    trailingContent = {
                        Text("Edit")
                    },
                    modifier = Modifier.fillMaxWidth().clickable {
                        onEdit.invoke(note.id)
                    }
                )
                HorizontalDivider()
            }
        }

    }
}