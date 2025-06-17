package com.finblue.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.finblue.domain.model.Asset
import com.finblue.domain.model.Portfolio
import com.finblue.viewmodel.AssetListUiState
import com.finblue.viewmodel.OperationUiState
import com.finblue.viewmodel.PortfolioViewModel
import kotlinx.datetime.Clock
import java.util.UUID

@Composable
fun CreateAssetDialog(
    selectedPortfolio: Portfolio?,
    viewModel: PortfolioViewModel,
    onAssetCreated: (Asset) -> Unit,
    onDismiss: () -> Unit
) {
    var symbol by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var assetType by remember { mutableStateOf("stock") }

    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

    // Handle asset creation success
    LaunchedEffect(operationState) {
        when (operationState) {
            is OperationUiState.Success -> {
                if ((operationState as OperationUiState.Success).message.contains("Asset")) {
                    // Find the created asset and return it
                    val assetState = viewModel.assetState.value
                    if (assetState is AssetListUiState.Success) {
                        val createdAsset = assetState.assets.find { it.symbol == symbol }
                        createdAsset?.let { onAssetCreated(it) }
                    }
                    viewModel.clearOperationState()
                }
            }
            else -> {}
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Asset") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedPortfolio == null) {
                    Text(
                        text = "Please select a portfolio first",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = symbol,
                    onValueChange = { symbol = it.uppercase() },
                    label = { Text("Symbol") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedPortfolio != null
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedPortfolio != null
                )

                // Asset Type Selection
                Column {
                    Text(
                        text = "Asset Type",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        listOf("stock", "crypto", "fiat", "other").forEach { type ->
                            FilterChip(
                                selected = assetType == type,
                                onClick = { assetType = type },
                                label = { Text(type.capitalize()) },
                                enabled = selectedPortfolio != null
                            )
                        }
                    }
                }

                // Error display
                if (operationState is OperationUiState.Error) {
                    Text(
                        text = (operationState as OperationUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (selectedPortfolio != null && symbol.isNotBlank() && name.isNotBlank()) {
                        val newAsset: Asset = when (assetType) {
                            "stock" -> Asset.Stock(
                                id = UUID.randomUUID().toString(),
                                portfolioId = selectedPortfolio.id,
                                symbol = symbol,
                                name = name,
                                createdAt = Clock.System.now().toEpochMilliseconds(),
                                exchange = "NASDAQ"
                            )
                            "crypto" -> Asset.Crypto(
                                id = UUID.randomUUID().toString(),
                                portfolioId = selectedPortfolio.id,
                                symbol = symbol,
                                name = name,
                                createdAt = Clock.System.now().toEpochMilliseconds(),
                                blockchain = "Ethereum"
                            )
                            "fiat" -> Asset.Fiat(
                                id = UUID.randomUUID().toString(),
                                portfolioId = selectedPortfolio.id,
                                symbol = symbol,
                                name = name,
                                createdAt = Clock.System.now().toEpochMilliseconds(),
                                country = "USA"
                            )
                            "other" -> Asset.Other(
                                id = UUID.randomUUID().toString(),
                                portfolioId = selectedPortfolio.id,
                                symbol = symbol,
                                name = name,
                                createdAt = Clock.System.now().toEpochMilliseconds(),
                                category = "collectible"
                            )
                            else -> return@TextButton // fail silently
                        }
                        viewModel.createAsset(newAsset)
                    }
                },
                enabled = selectedPortfolio != null && symbol.isNotBlank() && name.isNotBlank() &&
                        operationState !is OperationUiState.Loading
            ) {
                if (operationState is OperationUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text("Create")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}