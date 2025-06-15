package com.finblue

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.finblue.navigation.NavGraph
import com.finblue.viewmodel.PortfolioViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(viewModel: PortfolioViewModel = koinViewModel<PortfolioViewModel>()) {
    MaterialTheme {
        NavGraph()
    }
}