package com.jclemus.ui_app.di

import android.content.Context
import androidx.room.Room
import com.jclemus.ui_app.data.AppDatabase
import com.jclemus.ui_app.data.NotesRepository

object ServiceLocator {

    @Volatile private var db: AppDatabase? = null
    @Volatile private var repository: NotesRepository? = null

    fun repository(context: Context) : NotesRepository {
        val database = db ?: Room.databaseBuilder(
            context = context.applicationContext,
            klass = AppDatabase::class.java,
            name = "notes.db"
        ).build().also {
            db = it
        }

        return repository ?: NotesRepository(database.noteDao()).also { repository = it }
    }
}