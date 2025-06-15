package com.finblue.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.finblue.PortfolioScreen
import com.finblue.common.ErrorScreen
import com.finblue.common.ProgressScreen
import com.finblue.screens.CreateTransactionScreen
import com.finblue.viewmodel.PortfolioListUiState
import com.finblue.viewmodel.PortfolioViewModel
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
private fun MainScreenContent(
    navController: NavController,
    viewModel: PortfolioViewModel = koinViewModel<PortfolioViewModel>()
) {
    // Collect the portfolio state from the ViewModel
    val portfolioState by viewModel.portfolioState.collectAsStateWithLifecycle()

    when (val currentState = portfolioState) {
        is PortfolioListUiState.Loading -> ProgressScreen()
        is PortfolioListUiState.Error -> ErrorScreen(currentState.message) {
            viewModel.loadPortfolios()
        }

        is PortfolioListUiState.Success -> {
            PortfolioScreen(
                portfolios =  currentState.portfolios,
                onCreatePortfolio = { portfolio ->
                    viewModel.createPortfolio(portfolio)
                },
                onNavigateToCreateTransaction = {
                    navController.navigate(Route.CreateTransactionScreen.route)
                }
            )
        }
    }
}

@Composable
private fun PortfolioScreenContent(navController: NavController) {
    // TODO: Implement your portfolio screen content here
    // You can add your PortfolioDetailScreen composable here

    // Example placeholder - replace with your actual screen
    androidx.compose.material3.Text(
        text = "Portfolio Screen - TODO: Implement",
        modifier = androidx.compose.ui.Modifier.fillMaxSize()
    )
}

