package com.finblue.navigation.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.finblue.common.EmptyTransactionsMessage
import com.finblue.common.TransactionItem
import com.finblue.navigation.Route
import com.finblue.utils.Dimens.FB_16_dp
import com.finblue.utils.Dimens.FB_4_dp
import com.finblue.viewmodel.PortfolioViewModel
import com.finblue.viewmodel.TransactionListUiState
import io.ktor.http.hostIsIp
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PortfolioScreen(
    navController: NavController,
    portfolioId: String,
    viewModel: PortfolioViewModel = koinViewModel(),
) {
    val transactionState = viewModel.transactionState.collectAsState()
    val portfolioName = viewModel.getPortfolioById(portfolioId)?.name

    LaunchedEffect(portfolioId) {
        viewModel.loadTransactinsByPortfolio(portfolioId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = portfolioName ?: "Portfolio",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.removePortfolio(portfolioId)
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Portfolio")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.CreateTransactionScreen.route)
                },
                containerColor = Color(0xFF0055AA),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(horizontal =  FB_16_dp)) {
            when (val state = transactionState.value) {
                is TransactionListUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is TransactionListUiState.Error -> {
                    EmptyTransactionsMessage()
                }

                is TransactionListUiState.Success -> {
                    if (state.transactions.isEmpty()) {
                        EmptyTransactionsMessage()
                    } else {
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 510.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = state.transactions,
                                key = { it.id }
                            ) { transaction ->
                                TransactionItem(
                                    transaction = transaction,
                                    onRemove = viewModel::removeTransaction,
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = null,
                                        fadeOutSpec = null,
                                        placementSpec = spring(
                                            stiffness = Spring.StiffnessMediumLow,
                                            visibilityThreshold = IntOffset.VisibilityThreshold
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}