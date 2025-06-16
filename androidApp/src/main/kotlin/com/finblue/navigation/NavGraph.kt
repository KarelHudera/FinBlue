package com.finblue.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.finblue.PortfolioScreen
import com.finblue.common.ErrorScreen
import com.finblue.common.ProgressScreen
import com.finblue.viewmodel.PortfolioListUiState
import com.finblue.viewmodel.PortfolioViewModel
import com.finblue.viewmodel.TransactionListUiState
import kotlinx.coroutines.flow.combine
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavGraph(
    startDestination: String = Route.MainNavigation.route
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.MainNavigation.route,
            startDestination = Route.MainScreen.route
        ) {
            composable(route = Route.MainScreen.route) {
                MainScreenContent(navController = navController)
            }

            composable(route = Route.PortfolioScreen.route) {
                PortfolioScreenContent(navController = navController)
            }

            composable(route = Route.CreateTransactionScreen.route) {
                CreateTransactionScreen(navController = navController)
            }
        }
    }
}

@Composable
fun MainScreenContent(
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
            PortfolioScreen(
                portfolios = portfolioState.portfolios,
                onCreatePortfolio = viewModel::createPortfolio,
                onNavigateToCreateTransaction = {
                    navController.navigate(Route.CreateTransactionScreen.route)
                },
                transactions = transactions,
                onRemoveTransaction = viewModel::removeTransaction
            )
        }
    }
}

@Composable
private fun PortfolioScreenContent(navController: NavController) {
    // TODO: Implement your portfolio screen content here
    // You can add your PortfolioDetailScreen composable here

    // Example placeholder - replace with your actual screen
    Text(
        text = "Portfolio Screen - TODO: Implement",
        modifier = Modifier.fillMaxSize()
    )
}

