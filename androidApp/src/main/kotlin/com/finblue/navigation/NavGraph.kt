package com.finblue.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.finblue.navigation.screens.CreateTransactionScreen
import com.finblue.navigation.screens.MainScreen
import com.finblue.navigation.screens.PortfolioScreen

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
                MainScreen(navController = navController)
            }

            composable(
                route = Route.PortfolioScreen.route,
                arguments = listOf(
                    navArgument("portfolioId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val portfolioId = backStackEntry.arguments?.getString("portfolioId") ?: ""
                PortfolioScreen(
                    navController = navController,
                    portfolioId = portfolioId
                )
            }

            composable(route = Route.CreateTransactionScreen.route) {
                CreateTransactionScreen(navController = navController)
            }
        }
    }
}




