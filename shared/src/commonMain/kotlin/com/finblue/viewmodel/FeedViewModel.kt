package com.finblue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.finblue.domain.model.FeedWrapper
import com.finblue.domain.repository.BaseFeedRepository
import com.finblue.utils.Async

sealed class MovieListUiState {
    object Loading : MovieListUiState()
    data class Error(val message: String) : MovieListUiState()
    data class Success(val result: List<FeedWrapper>) : MovieListUiState()
}

class FeedViewModel(
    private val repository: BaseFeedRepository,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val stateFlow: StateFlow<MovieListUiState>
        get() = _stateFlow

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            repository.getResult().collect { uiState ->
                _stateFlow.tryEmit(
                    when (uiState) {
                        is Async.Loading -> MovieListUiState.Loading
                        is Async.Error -> MovieListUiState.Error(uiState.message)
                        is Async.Success -> MovieListUiState.Success(uiState.data)
                    }
                )
            }
        }
    }
}