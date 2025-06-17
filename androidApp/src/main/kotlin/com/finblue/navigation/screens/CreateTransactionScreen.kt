package com.finblue.navigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.finblue.common.CreateAssetDialog
import com.finblue.common.PortfolioDropdown
import com.finblue.common.TransactionTypeSelector
import com.finblue.domain.model.Asset
import com.finblue.domain.model.Portfolio
import com.finblue.domain.model.Transaction
import com.finblue.viewmodel.AssetListUiState
import com.finblue.viewmodel.OperationUiState
import com.finblue.viewmodel.PortfolioListUiState
import com.finblue.viewmodel.PortfolioViewModel
import kotlinx.datetime.Clock
import org.koin.compose.viewmodel.koinViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionScreen(
    viewModel: PortfolioViewModel = koinViewModel<PortfolioViewModel>(),
    navController: NavController
) {
    val portfolioState by viewModel.portfolioState.collectAsStateWithLifecycle()
    val assetState by viewModel.assetState.collectAsStateWithLifecycle()
    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

    var selectedPortfolio by remember { mutableStateOf<Portfolio?>(null) }
    var selectedAsset by remember { mutableStateOf<Asset?>(null) }
    var transactionType by remember { mutableStateOf("buy") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("USD") }
    var notes by remember { mutableStateOf("") }
    var showCreateAssetDialog by remember { mutableStateOf(false) }

    // Handle operation state
    LaunchedEffect(operationState) {
        when (operationState) {
            is OperationUiState.Success -> {
                if ((operationState as OperationUiState.Success).message.contains("Transaction")) {
                    navController.popBackStack()                }
                viewModel.clearOperationState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Transaction") },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Portfolio Selection
            when (val state = portfolioState) {
                is PortfolioListUiState.Success -> {
                    PortfolioDropdown(
                        portfolios = state.portfolios,
                        selectedPortfolio = selectedPortfolio,
                        onPortfolioSelected = { selectedPortfolio = it }
                    )
                }
                is PortfolioListUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is PortfolioListUiState.Error -> {
                    Text(
                        text = "Error loading portfolios: ${state.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Asset Selection
            when (val state = assetState) {
                is AssetListUiState.Success -> {
                    AssetSelection(
                        assets = state.assets,
                        selectedAsset = selectedAsset,
                        onAssetSelected = { selectedAsset = it },
                        onCreateNewAsset = { showCreateAssetDialog = true }
                    )
                }
                is AssetListUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is AssetListUiState.Error -> {
                    Text(
                        text = "Error loading assets: ${state.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            TransactionTypeSelector(
                selectedType = transactionType,
                onTypeSelected = { transactionType = it }
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price per unit") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = currency,
                onValueChange = { currency = it },
                label = { Text("Currency") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Create Transaction Button
            Button(
                onClick = {
                    if (selectedPortfolio != null && selectedAsset != null &&
                        quantity.isNotBlank() && price.isNotBlank()) {

                        val transaction = Transaction(
                            id = UUID.randomUUID().toString(),
                            portfolioId = selectedPortfolio!!.id,
                            assetId = selectedAsset!!.id,
                            type = transactionType,
                            quantity = quantity.toDoubleOrNull() ?: 0.0,
                            price = price.toDoubleOrNull() ?: 0.0,
                            currency = currency,
                            notes = notes.ifBlank { null },
                            transactionDate = Clock.System.now().toEpochMilliseconds(),
                            createdAt = Clock.System.now().toEpochMilliseconds()
                        )

                        viewModel.addTransaction(transaction)
                        viewModel.loadTransactions()
                    }
                },
                enabled = selectedPortfolio != null && selectedAsset != null &&
                        quantity.isNotBlank() && price.isNotBlank() &&
                        operationState !is OperationUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (operationState is OperationUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create Transaction")
                }
            }

            // Error display
            if (operationState is OperationUiState.Error) {
                Text(
                    text = (operationState as OperationUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    // Create Asset Dialog
    if (showCreateAssetDialog) {
        CreateAssetDialog(
            selectedPortfolio = selectedPortfolio,
            viewModel = viewModel,
            onAssetCreated = { asset ->
                selectedAsset = asset
                showCreateAssetDialog = false
            },
            onDismiss = { showCreateAssetDialog = false }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssetSelection(
    assets: List<Asset>,
    selectedAsset: Asset?,
    onAssetSelected: (Asset) -> Unit,
    onCreateNewAsset: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedAsset?.let { "${it.symbol} - ${it.name}" } ?: "Select Asset",
                onValueChange = {},
                readOnly = true,
                label = { Text("Asset") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                assets.forEach { asset ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(asset.symbol)
                                Text(
                                    text = asset.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        onClick = {
                            onAssetSelected(asset)
                            expanded = false
                        }
                    )
                }

                HorizontalDivider()

                DropdownMenuItem(
                    text = { Text("+ Create New Asset") },
                    onClick = {
                        onCreateNewAsset()
                        expanded = false
                    }
                )
            }
        }
    }
}