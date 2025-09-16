@file:OptIn(ExperimentalMaterial3Api::class)

package com.jclemus.ui_app.feature.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jclemus.ui_app.feature.NotesViewModel

@Composable
fun NoteEditorScreen(
    viewModel: NotesViewModel,
    noteId: Long?,
    onDone: () -> Boolean,
    onPersistUri: (uri: String) -> Unit
) {

    val existing by viewModel.note(id = noteId).collectAsState(initial = null)

    var title by rememberSaveable(noteId) { mutableStateOf("") }
    var body by rememberSaveable(noteId) { mutableStateOf( "") }
    var imageUri by rememberSaveable(noteId) { mutableStateOf<String?>(null) }

    LaunchedEffect(noteId, existing?.id) {
        if (noteId != null && existing != null) {
            title = existing!!.title
            body = existing!!.body
            imageUri = existing!!.imageUri
        }
    }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri.toString()
            onPersistUri(imageUri!!)
        }

    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (noteId == null) "New Note" else "Edit Note") }) }
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title") },
                singleLine = true
            )
            OutlinedTextField(
                value = body,
                onValueChange = { body = it},
                modifier = Modifier.fillMaxWidth().weight(1f),
                label = { Text("Content")}
            )

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier.height(160.dp).fillMaxWidth()
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = {
                    picker.launch(arrayOf("image/*"))
                }) {
                    Text(
                        if (imageUri == null) {
                            "Attach image"
                        } else {
                            "Change image"
                        }
                    )
                }
                if (imageUri != null) {
                    OutlinedButton(
                        onClick = {
                            imageUri = null
                        }
                    ) {
                        Text("Remove image")
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val canSave = title.isNotBlank() || body.isNotBlank() || imageUri != null
                Button(
                    enabled = canSave,
                    onClick = {
                        viewModel.save(
                            id = noteId,
                            title = title,
                            body = body,
                            imageUri = imageUri
                        )
                    }
                ) {
                    Text("Save")
                }

                if (noteId != null) {
                    OutlinedButton(
                        onClick = {
                            viewModel.delete(noteId); onDone()
                        }
                    ) {
                        Text("Delete ")
                    }
                }
            }
        }
    }
}