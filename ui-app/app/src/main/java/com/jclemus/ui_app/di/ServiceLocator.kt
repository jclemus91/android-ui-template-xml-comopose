package com.jclemus.ui_app.di

import android.content.Context
import androidx.room.Room
import com.jclemus.ui_app.data.AppDatabase
import com.jclemus.ui_app.data.NotesDao
import com.jclemus.ui_app.data.NotesRepository

object ServiceLocator {

    @Volatile private var db: AppDatabase? = null
    @Volatile private var repo: NotesRepository? = null

    fun getRepository(context: Context): NotesRepository {
        val database = db ?: Room.databaseBuilder(context, AppDatabase::class.java, "notes.db").build().also {
            db = it
        }
        val repository = repo ?: NotesRepository(database.notesDao()).also {
            repo = it
        }
        return repository
    }


}