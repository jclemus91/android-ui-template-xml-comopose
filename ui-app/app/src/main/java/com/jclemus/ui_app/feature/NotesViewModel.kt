package com.jclemus.ui_app.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.jclemus.ui_app.data.Note
import com.jclemus.ui_app.data.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UiState(
    val query: String = "",
    val notes: List<Note> = emptyList(),
    val loading: Boolean = false
)

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val query = MutableStateFlow("")
    private val loading = MutableStateFlow(false)

    val uiState: StateFlow<UiState> =
        query.flatMapLatest { q -> if (q.isBlank()) repository.getAll() else repository.search(q) }
            .combine(query) { notes, query ->
                UiState(
                    query = query,
                    notes = notes,
                    loading = loading.value
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), UiState())

    fun search(query: String) {
        this.query.value = query
    }

    fun getNote(id: Long?): Flow<Note?> {
        return if (id == null) {
            flowOf(null)
        } else {
            repository.getById(id)
        }
    }

    fun save(
        id: Long?,
        title: String,
        body: String,
        imageUri: String?
    ) = viewModelScope.launch {
        loading.value = true
        val now = System.currentTimeMillis()
        val note = Note(
            id = id ?: 0,
            title = title,
            body = body,
            imageUri = imageUri,
            updatedAt = now
        )
        repository.updateInsert(note)
        loading.value = false
    }

    fun delete(noteId: Long)= viewModelScope.launch {
        repository.delete(noteId)
    }
}