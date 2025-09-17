package com.jclemus.ui_app.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jclemus.ui_app.data.NotesRepository

class VMFactory(private val repository: NotesRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotesViewModel(repository) as T
    }
}