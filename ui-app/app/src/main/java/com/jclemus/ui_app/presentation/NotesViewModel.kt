package com.jclemus.ui_app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jclemus.ui_app.domain.Note
import com.jclemus.ui_app.domain.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    val notes: StateFlow<List<Note>> = repository.notes

    fun getNote(id: Long): Flow<Note?> {
        return repository.getNote(id)
    }

    fun add(
        title: String,
        body: String,
        imageUri: String? = null
    ) {
        viewModelScope.launch {
            repository.add(
                title = title,
                body = body,
                imageUri = imageUri
            )
        }
    }

    fun update(
        id: Long,
        title: String,
        body: String,
        imageUri: String? = null
    ) {
        viewModelScope.launch {
            repository.update(
                id = id,
                title = title,
                body = body,
                imageUri = imageUri
            )
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch { repository.delete(note)  }
    }
}