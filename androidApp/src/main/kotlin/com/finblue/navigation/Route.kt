package com.finblue.navigation

import androidx.navigation.NamedNavArgument

/**
 * A sealed class representing the different routes in the app's navigation system.
 *
 * Each route corresponds to a specific screen or destination within the app. The route's
 * `route` string is used for navigation, and `arguments` can be added to pass data
 * between destinations if needed.
 */
sealed class Route(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object MainNavigation : Route(route = "mainNavigation")

    data object MainScreen : Route(route = "mainScreen")

    data object PortfolioScreen : Route("portfolio_screen/{portfolioId}") {
        fun createRoute(portfolioId: String) = "portfolio_screen/$portfolioId"
    }

    data object CreateTransactionScreen : Route(route = "createTransactionScreen")
}