package com.finblue.navigation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.finblue.common.ErrorScreen
import com.finblue.common.ProgressScreen
import com.finblue.viewmodel.PortfolioViewModel
import org.koin.compose.viewmodel.koinViewModel

// 3. Update your PortfolioScreen to receive portfolio ID
@Composable
fun PortfolioScreen(
    navController: NavController,
    portfolioId: String,
    viewModel: PortfolioViewModel = koinViewModel()
) {
    Text(
        text = "Portfolio:}",
        style = MaterialTheme.typography.headlineMedium
    )
    Text(
        text = "ID: $portfolioId",
        style = MaterialTheme.typography.bodyMedium
    )
}