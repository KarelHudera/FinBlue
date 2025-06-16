package com.finblue.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.finblue.domain.model.Portfolio
import com.finblue.domain.model.Transaction
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    portfolios: List<Portfolio>,
    transactions: List<Transaction>,
    onCreatePortfolio: (Portfolio) -> Unit,
    onNavigateToCreateTransaction: () -> Unit,
    onPortfolioClick: (String) -> Unit,
    onRemoveTransaction: (String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToCreateTransaction()
                },
                containerColor = Color(0xFF0055AA),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) {
        ScrollTopBar {
            FeedCollectionList (
                collection = portfolios,
                transactions = transactions,
                onAddPortfolioClick = { showBottomSheet = true },
                onPortfolioClick = onPortfolioClick,
                onRemoveTransaction = onRemoveTransaction,
                modifier = Modifier.padding(it)
            )
        }
    }

    // Bottom Sheet for creating new portfolio
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            CreatePortfolioBottomSheet(
                onCreatePortfolio = { portfolio ->
                    onCreatePortfolio(portfolio)
                    scope.launch {
                        bottomSheetState.hide()
                        showBottomSheet = false
                    }
                },
                onDismiss = {
                    scope.launch {
                        bottomSheetState.hide()
                        showBottomSheet = false
                    }
                }
            )
        }
    }
}