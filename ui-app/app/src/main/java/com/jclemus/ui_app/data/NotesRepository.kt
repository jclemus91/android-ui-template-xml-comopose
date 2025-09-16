package com.jclemus.ui_app.data

import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class NotesRepository(
    private val notesDao: NotesDao
) {

    fun getNotes(query: String? = null): Flow<List<Note>> {
        return if (query != null) {
            notesDao.search(query)
        } else {
            notesDao.getNotes()
        }
    }

    fun getNoteById(id: Long): Flow<Note?> {
        return notesDao.getNoteById(id)
    }

    suspend fun save(
        id: Long?,
        title: String,
        body: String,
        imageUri: String?
    ): Long {
        val now = Calendar.getInstance().time.time
        val note = Note(
            id = id ?: 0,
            title = title,
            body = body,
            imageUri = imageUri,
            updatedAt = now
        )

        return notesDao.update(note)
    }

    suspend fun delete(id: Long) = notesDao.delete(id)

}