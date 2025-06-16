package com.finblue.navigation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.finblue.common.ErrorScreen
import com.finblue.common.MainScreenContent
import com.finblue.common.ProgressScreen
import com.finblue.navigation.Route
import com.finblue.viewmodel.PortfolioListUiState
import com.finblue.viewmodel.PortfolioViewModel
import com.finblue.viewmodel.TransactionListUiState
import kotlinx.coroutines.flow.combine
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: PortfolioViewModel = koinViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val combinedState by remember {
        combine(
            viewModel.portfolioState,
            viewModel.transactionState
        ) { portfolioState, transactionState ->
            portfolioState to transactionState
        }
    }.collectAsStateWithLifecycle(
        initialValue = PortfolioListUiState.Loading to TransactionListUiState.Loading,
        lifecycle = lifecycleOwner.lifecycle
    )

    val (portfolioState, transactionState) = combinedState

    when (portfolioState) {
        is PortfolioListUiState.Loading -> ProgressScreen()

        is PortfolioListUiState.Error -> ErrorScreen(
            message = portfolioState.message,
            refresh = viewModel::loadInitialData
        )

        is PortfolioListUiState.Success -> {
            viewModel.loadInitialData()

            val transactions = when (transactionState) {
                is TransactionListUiState.Success -> transactionState.transactions
                else -> emptyList()
            }
            MainScreenContent(
                portfolios = portfolioState.portfolios,
                onCreatePortfolio = viewModel::createPortfolio,
                onNavigateToCreateTransaction = {
                    navController.navigate(Route.CreateTransactionScreen.route)
                },
                onPortfolioClick = { portfolioId ->
                    navController.navigate(Route.PortfolioScreen.createRoute(portfolioId))
                },
                transactions = transactions,
                onRemoveTransaction = viewModel::removeTransaction
            )
        }
    }
}