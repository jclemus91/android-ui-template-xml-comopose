package com.jclemus.androidsamples.feature

import androidx.lifecycle.ViewModel
import com.jclemus.androidsamples.data.ContactRepository
import com.jclemus.androidsamples.domain.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class UiState(
    val contacts: List<Contact> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

class ContactViewModel(
    private val repository: ContactRepository = ContactRepository()
) : ViewModel(){

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        _uiState.update {
            it.copy(contacts = repository.getAll(), isLoading = false)
        }
    }

    fun updateSearchQuery(query: String) {
        val filteredContacts = if (query.isBlank()) {
            repository.getAll()
        } else {
            repository.getFilteredContacts(query)
        }
        _uiState.update {
            it.copy(contacts = filteredContacts, searchQuery = query)
        }
    }

    fun getContactById(id: Long): Contact? {
        return repository.getContactById(id)
    }
}