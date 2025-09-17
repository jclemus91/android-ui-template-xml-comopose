package com.jclemus.ui_app

import com.jclemus.ui_app.data.InMemoryNoteRepository
import com.jclemus.ui_app.domain.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesModule {

    @Provides
    @Singleton
    fun providesNotesRepository(): NotesRepository = InMemoryNoteRepository()
}