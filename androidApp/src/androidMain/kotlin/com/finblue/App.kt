package com.finblue

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.finblue.common.ErrorScreen
import com.finblue.common.ProgressScreen
import com.finblue.viewmodel.FeedViewModel
import com.finblue.viewmodel.MovieListUiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(viewModel: FeedViewModel = koinViewModel<FeedViewModel>()) {
    MaterialTheme {
        val state by viewModel.stateFlow.collectAsStateWithLifecycle()
        when(val currentState = state) {
            is MovieListUiState.Loading -> ProgressScreen()
            is MovieListUiState.Error -> ErrorScreen(currentState.message) { viewModel.refresh() }
            is MovieListUiState.Success -> FeedScreen(currentState.result)
        }
    }
}