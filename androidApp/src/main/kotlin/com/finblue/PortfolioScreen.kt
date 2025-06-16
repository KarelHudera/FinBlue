package com.finblue

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.finblue.common.ScrollTopBar
import com.finblue.domain.model.Asset
import com.finblue.domain.model.Portfolio
import com.finblue.domain.model.Transaction
import com.finblue.utils.Dimens.FB_10_dp
import com.finblue.utils.Dimens.FB_120_dp
import com.finblue.utils.Dimens.FB_12_dp
import com.finblue.utils.Dimens.FB_16_dp
import com.finblue.utils.Dimens.FB_180_dp
import com.finblue.utils.Dimens.FB_20_dp
import com.finblue.utils.Dimens.FB_220_dp
import com.finblue.utils.Dimens.FB_2_dp
import com.finblue.utils.Dimens.FB_32_dp
import com.finblue.utils.Dimens.FB_36_dp
import com.finblue.utils.Dimens.FB_4_dp
import com.finblue.utils.Dimens.FB_6_dp
import com.finblue.utils.pagerTransition
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.UUID


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    portfolios: List<Portfolio>,
    transactions: List<Transaction>,
    onCreatePortfolio: (Portfolio) -> Unit = {},
    onNavigateToCreateTransaction: () -> Unit = {},
    onRemoveTransaction: (String) -> Unit = {}
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
            FeedCollectionList(
                collection = portfolios,
                transactions = transactions,
                onAddPortfolioClick = { showBottomSheet = true },
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

@Composable
private fun FeedCollectionList(
    collection: List<Portfolio>,
    onAddPortfolioClick: () -> Unit,
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
                modifier = modifier
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

@Composable
private fun TransactionsSection(
    transactions: List<Transaction>,
    onRemoveTransaction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transactions",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "${transactions.size} transactions",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        if (transactions.isEmpty()) {
            EmptyTransactionsMessage()
        } else {
            LazyColumn(
                modifier = Modifier.heightIn(max = 510.dp), // Limit height to prevent infinite expansion
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = transactions,
                    key = { it.id }
                ) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onRemove = { onRemoveTransaction(transaction.id) },
                        modifier = Modifier.animateItem(
                            fadeInSpec = null, fadeOutSpec = null, placementSpec = spring(
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

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionItem(
    transaction: Transaction,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$${String.format("%.2f", transaction.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = transaction.type,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (transaction.type == "buy")
                            Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${transaction.quantity} shares",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                }

                IconButton (
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete transaction",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total: $${
                        String.format(
                            "%.2f",
                            transaction.quantity * transaction.price
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = formatDate(transaction.transactionDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction? This action cannot be undone.") },
            confirmButton = {
                TextButton (
                    onClick = {
                        onRemove()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun EmptyTransactionsMessage() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No transactions yet",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Add your first transaction using the + button",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Helper function to format date (you might need to adjust this based on your date format)
private fun formatDate(timestamp: Long): String {
    // This is a simple implementation - you might want to use a proper date formatting library
    return java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        .format(java.util.Date(timestamp))
}

@Composable
private fun AddPortfolioCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(FB_180_dp)
            .clip(RoundedCornerShape(FB_10_dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(FB_10_dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x220055AA)), // Light faded blue background
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0055AA),
            )
        }
    }
}

@Composable
private fun PortfolioCarousel(
    portfolios: List<Portfolio>,
    onAddPortfolioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { portfolios.size + 1 })

    Header(title = "Portfolios", modifier = modifier)

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = FB_16_dp),
    ) { page ->
        if (page < portfolios.size) {
            with(portfolios[page]) {
                TrendingItem(
                    title = name,
                    currency = baseCurrency,
                    modifier = Modifier.pagerTransition(
                        pagerState = pagerState,
                        page = page,
                    )
                )
            }
        } else {
            AddPortfolioCard(
                onClick = onAddPortfolioClick,
                modifier = Modifier.pagerTransition(
                    pagerState = pagerState,
                    page = page,
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(FB_20_dp))

    // Page indicator dots
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) {
                Color(0xFF0055AA)
            } else {
                Color.LightGray
            }
            Box(
                modifier = Modifier
                    .padding(FB_4_dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(FB_6_dp),
            )
        }
    }
}

@Composable
private fun CreatePortfolioBottomSheet(
    onCreatePortfolio: (Portfolio) -> Unit,
    onDismiss: () -> Unit
) {
    var portfolioName by remember { mutableStateOf("") }
    var brokerBank by remember { mutableStateOf("") }
    var baseCurrency by remember { mutableStateOf("USD") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(FB_16_dp),
        verticalArrangement = Arrangement.spacedBy(FB_16_dp)
    ) {
        Text(
            text = "Create New Portfolio",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = portfolioName,
            onValueChange = { portfolioName = it },
            label = { Text("Portfolio Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = brokerBank,
            onValueChange = { brokerBank = it },
            label = { Text("Broker/Bank (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = baseCurrency,
            onValueChange = { baseCurrency = it },
            label = { Text("Base Currency") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FB_12_dp)
        ) {
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    if (portfolioName.isNotBlank()) {
                        val portfolio = Portfolio(
                            id = UUID.randomUUID().toString(),
                            name = portfolioName.trim(),
                            brokerBank = brokerBank.trim().takeIf { it.isNotBlank() },
                            baseCurrency = baseCurrency.trim(),
                            description = description.trim().takeIf { it.isNotBlank() },
                            createdAt = Clock.System.now().toEpochMilliseconds(),
                        )
                        onCreatePortfolio(portfolio)
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = portfolioName.isNotBlank()
            ) {
                Text("Create")
            }
        }

        // Add some bottom padding for the bottom sheet
        Spacer(modifier = Modifier.height(FB_32_dp))
    }
}

@Composable
fun TrendingItem(
    title: String,
    currency: String,
    modifier: Modifier = Modifier,
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF00AFFF),
            Color(0xFF0055AA)
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(FB_180_dp)
            .clip(RoundedCornerShape(FB_10_dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = FB_12_dp, bottom = FB_6_dp)
                    .align(Alignment.BottomStart),
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(FB_6_dp))
                Text(
                    text = currency,
                    color = Color.White,
                )
            }
        }
    }
}


@Composable
private fun Header(title: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = FB_36_dp)
            .padding(start = FB_12_dp),
    ) {
        Text(
            text = title,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
        )
    }
}

@Composable
private fun Feeds(feeds: List<Portfolio>, index: Int, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = FB_2_dp, end = FB_2_dp),
    ) {
        items(feeds) { feed ->
            if (index % 3 == 0) {
                FBCard(feed, feed.id, FB_220_dp)
            } else {
                FBCard(feed, feed.name, FB_120_dp)
            }
        }
    }
}

@Composable
private fun FBCard(
    movie: Portfolio,
    imageUrl: String?,
    itemWidth: Dp
) {
    Card(
        modifier = Modifier.padding(FB_6_dp),
        shape = RoundedCornerShape(FB_10_dp),
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = movie.name,
                modifier = Modifier
                    .size(width = itemWidth, height = FB_180_dp),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = movie.name,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .size(width = itemWidth, height = FB_36_dp)
                    .wrapContentHeight(),
            )
        }
    }
}

@Composable
private fun EmptyPortfolioMessage() {
    Text(
        text = "This portfolio has no assets.",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FB_12_dp, horizontal = FB_16_dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium
    )
}