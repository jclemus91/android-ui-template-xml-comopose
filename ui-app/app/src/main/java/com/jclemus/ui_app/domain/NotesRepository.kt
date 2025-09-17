package com.jclemus.ui_app.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NotesRepository {

    val notes: StateFlow<List<Note>>
    fun getNote(id: Long): Flow<Note?>
    suspend fun add(
        title: String,
        body: String,
        imageUri: String? = null
    ): Note
    suspend fun update(
        id: Long,
        title: String,
        body: String,
        imageUri: String? = null
    ): Note
    suspend fun delete(note: Note)
}