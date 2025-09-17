@file:OptIn(ExperimentalMaterial3Api::class)

package com.jclemus.ui_app.feature.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
    onDone: () -> Unit,
    onPersistUri: (String) -> Unit
) {
    val existedNote by viewModel.getNote(noteId).collectAsState(initial = null)

    var title by rememberSaveable(noteId) {
        mutableStateOf("")
    }
    var body by rememberSaveable(noteId) {
        mutableStateOf("")
    }
    var imageUri by rememberSaveable(noteId) {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(noteId, existedNote) {
        if (noteId != null && existedNote != null && title.isEmpty() && body.isEmpty() && imageUri == null) {
            title = existedNote!!.title
            body = existedNote!!.body
            imageUri = existedNote!!.imageUri
        }
    }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri.toString()
            onPersistUri.invoke(imageUri!!)
        }
    }

    val canSave = title.isNotBlank() || body.isNotBlank() || imageUri != null

    Scaffold(
        topBar = { TopAppBar(title={ Text(if (noteId == null) "New Note" else "Edit Note") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = body,
                onValueChange = {
                    body = it
                },
                modifier = Modifier.fillMaxWidth().weight(1f)
            )

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp).fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        picker.launch(arrayOf("image/*"))
                    }
                ) {
                    Text(if (imageUri == null) "Attach Image" else "Change Image")
                }
                if (imageUri != null) {
                    Button(
                        onClick = {
                            imageUri = null
                        }
                    ) {
                        Text("Remove Image")
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    enabled = canSave,
                    onClick = {
                        viewModel.save(
                            id = noteId,
                            title = title,
                            body = body,
                            imageUri = imageUri
                        )
                        onDone.invoke()
                    }
                ) {
                    Text("Save")
                }

                if (noteId != null) {
                    OutlinedButton(
                        onClick = {
                            viewModel.delete(noteId)
                            onDone.invoke()
                        }
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}
