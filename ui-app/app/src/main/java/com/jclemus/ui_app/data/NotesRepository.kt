package com.jclemus.ui_app.data

import kotlinx.coroutines.flow.Flow

class NotesRepository(
    private val dao: NotesDao
) {

    fun getAll(): Flow<List<Note>> {
        return dao.getAll()
    }

    fun getById(id: Long): Flow<Note?> {
        return dao.getById(id)
    }

    fun search(query: String): Flow<List<Note>> {
        return dao.search(query)
    }

    suspend fun updateInsert(note: Note): Long {
        return dao.updateInsert(note)
    }

    suspend fun delete(id: Long) {
        dao.delete(id)
    }
}