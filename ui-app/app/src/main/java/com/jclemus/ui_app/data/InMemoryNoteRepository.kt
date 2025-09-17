package com.jclemus.ui_app.data

import com.jclemus.ui_app.domain.Note
import com.jclemus.ui_app.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicLong

class InMemoryNoteRepository(
    mock : List<Note> = listOf(
        Note(
            id = 1,
            title = "Title 1",
            body = "Body",
            imageUri = null
        ),
        Note(
            id = 2,
            title = "Title 2",
            body = "Body",
            imageUri = null
        )
    )

): NotesRepository {

    val idGenerator = AtomicLong(mock.maxOfOrNull { it.id }?.plus(1) ?: 1)
    val store = mock.associateBy { it.id }.toMutableMap()

    private val _notes = MutableStateFlow(store.values.sortedByDescending { it.updatedAt })
    override val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    override fun getNote(id: Long): Flow<Note?> {
        return notes.map { it.firstOrNull { it.id == id } }.distinctUntilChanged()
    }

    override suspend fun add(
        title: String,
        body: String,
        imageUri: String?
    ): Note {
        val newNote = Note(
            id = idGenerator.andDecrement,
            title = title,
            body = body,
            imageUri = imageUri
        )
        store.put(newNote.id, newNote)

        _notes.value = store.values.sortedByDescending { it.updatedAt }

        return newNote
    }

    override suspend fun update(
        id: Long,
        title: String,
        body: String,
        imageUri: String?
    ): Note {
        val current = store[id]
        val updated = current?.copy(
            title = title,
            body = body,
            imageUri = imageUri
        )
        store[id] = updated!!

        _notes.value = store.values.sortedByDescending { it.updatedAt }

        return updated
    }

    override suspend fun delete(note: Note) {
        store.remove(note.id)

        _notes.value = store.values.sortedByDescending { it.updatedAt }
    }
}