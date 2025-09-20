package com.jclemus.ui_app.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jclemus.ui_app.data.SectionsRepository
import com.jclemus.ui_app.domain.Home
import com.jclemus.ui_app.domain.Section
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn


data class UiState(
    val sections: List<Section> = emptyList(),
    val isLoading: Boolean = false,
    val query: String = ""
)

class ExploreViewModel(
    private val repository: SectionsRepository = SectionsRepository()
): ViewModel() {

    private val query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<UiState> = query.flatMapLatest { query: String ->
        repository.getSections(query)
    }.combine(query) { sections, query ->
        UiState(
            sections = sections,
            isLoading = false,
            query = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun setQuery(q: String) {
        query.value = q
    }

    fun getHome(id: Long): Home? {
        return repository.getHome(id)
    }
}