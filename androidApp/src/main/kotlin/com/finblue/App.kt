package com.finblue

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.finblue.common.ErrorScreen
import com.finblue.common.ProgressScreen
import com.finblue.viewmodel.PortfolioListUiState
import com.finblue.viewmodel.PortfolioViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(viewModel: PortfolioViewModel = koinViewModel<PortfolioViewModel>()) {
    MaterialTheme {
        // Collect the portfolio state from the ViewModel
        val portfolioState by viewModel.portfolioState.collectAsStateWithLifecycle()

        when(val currentState = portfolioState) {
            is PortfolioListUiState.Loading -> ProgressScreen()
            is PortfolioListUiState.Error -> ErrorScreen(currentState.message) {
                viewModel.loadPortfolios()
            }
            is PortfolioListUiState.Success -> {
                PortfolioScreen(currentState.portfolios)
            }
        }
    }
}