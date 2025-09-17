package com.jclemus.ui_app.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun notesDao(): NotesDao
}