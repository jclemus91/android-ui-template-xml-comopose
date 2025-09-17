@file:OptIn(ExperimentalMaterial3Api::class)

package com.jclemus.ui_app.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotesEditorScreen(viewModel: NotesViewModel, noteId: Long, onDone: () -> Boolean) {

    val existing by remember(noteId) {
        viewModel.getNote(noteId)
    }.collectAsState(initial = null)

    var title by rememberSaveable(noteId) { mutableStateOf(existing?.title.orEmpty()) }
    var body by rememberSaveable(noteId) { mutableStateOf(existing?.body.orEmpty()) }

    LaunchedEffect(existing?.id, noteId) {
        if (existing != null) {
            title = existing!!.title
            body = existing!!.body
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(if (existing == null) "New Note" else "Edit Note")
            })
    }, bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    if (existing == null) {
                        viewModel.add(title, body)
                        onDone.invoke()
                    } else {
                        viewModel.update(noteId, title, body)
                        onDone.invoke()
                    }
                }) {
                Text("Save")
            }
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(value = title, onValueChange = { title = it })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = body, onValueChange = {body = it }, minLines = 6)
        }

    }

}