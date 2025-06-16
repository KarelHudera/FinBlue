package com.finblue.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.finblue.domain.model.Portfolio
import com.finblue.domain.model.Transaction

@Composable
fun FeedCollectionList(
    collection: List<Portfolio>,
    onAddPortfolioClick: () -> Unit,
    onPortfolioClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    transactions: List<Transaction>,
    onRemoveTransaction: (String) -> Unit,
) {
    if (collection.isEmpty()) {
        EmptyPortfolioMessage()
        return
    }

    LazyColumn {
        item {
            PortfolioCarousel(
                portfolios = collection,
                onAddPortfolioClick = onAddPortfolioClick,
                modifier = modifier,
                onPortfolioClick = onPortfolioClick
            )
        }
        item {
            TransactionsSection(
                transactions = transactions,
                onRemoveTransaction = onRemoveTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}