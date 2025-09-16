@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jclemus.ui_app.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jclemus.ui_app.data.Note
import com.jclemus.ui_app.data.NotesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NotesUiState(
    val query: String = "",
    val notes: List<Note> = emptyList(),
    val loading: Boolean = false
)

class NotesViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    private val query = MutableStateFlow("")
    private val loading = MutableStateFlow(false)

    val uiSate: StateFlow<NotesUiState>
        get() = query.flatMapLatest { query ->
            notesRepository.getNotes(query)
        }.combine(query) { notes, query ->
            NotesUiState(
                query = query,
                notes = notes,
                loading = loading.value
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(4000), NotesUiState())

    fun searchQuery(query: String) {
        this.query.value = query
    }

    fun save(
        id: Long?,
        title: String,
        body: String,
        imageUri: String?
    ) = viewModelScope.launch {
        loading.value = true
        notesRepository.save(id, title, body, imageUri)
    }

    fun delete(id: Long) = viewModelScope.launch {
        notesRepository.delete(id)
    }

    fun note(id: Long?) : Flow<Note?> {
        return if (id == null) flowOf(null) else notesRepository.getNoteById(id).distinctUntilChanged()
    }
}