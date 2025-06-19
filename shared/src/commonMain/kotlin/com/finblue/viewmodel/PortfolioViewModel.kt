package com.finblue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finblue.data.repository.PortfolioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.finblue.domain.model.Portfolio
import com.finblue.domain.model.Transaction
import com.finblue.domain.model.Asset
import com.finblue.utils.Async
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock


// UI States
sealed class PortfolioListUiState {
    object Loading : PortfolioListUiState()
    data class Error(val message: String) : PortfolioListUiState()
    data class Success(val portfolios: List<Portfolio>) : PortfolioListUiState()
}

sealed class PortfolioDetailUiState {
    object Loading : PortfolioDetailUiState()
    data class Error(val message: String) : PortfolioDetailUiState()
    data class Success(val portfolio: Portfolio) : PortfolioDetailUiState()
}

sealed class TransactionListUiState {
    object Loading : TransactionListUiState()
    data class Error(val message: String) : TransactionListUiState()
    data class Success(val transactions: List<Transaction>) : TransactionListUiState()
}

sealed class AssetListUiState {
    object Loading : AssetListUiState()
    data class Error(val message: String) : AssetListUiState()
    data class Success(val assets: List<Asset>) : AssetListUiState()
}

sealed class OperationUiState {
    object Idle : OperationUiState()
    object Loading : OperationUiState()
    data class Success(val message: String) : OperationUiState()
    data class Error(val message: String) : OperationUiState()
}

class PortfolioViewModel(
    private val repository: PortfolioRepository,
) : ViewModel() {

    // Portfolio state
    private val _portfolioState =
        MutableStateFlow<PortfolioListUiState>(PortfolioListUiState.Loading)
    val portfolioState: StateFlow<PortfolioListUiState> = _portfolioState.asStateFlow()

    // Portfolio detail state (for single portfolio)
    private val _portfolioDetailState =
        MutableStateFlow<PortfolioDetailUiState>(PortfolioDetailUiState.Loading)
    val portfolioDetailState: StateFlow<PortfolioDetailUiState> =
        _portfolioDetailState.asStateFlow()

    // Transaction state
    private val _transactionState =
        MutableStateFlow<TransactionListUiState>(TransactionListUiState.Loading)
    val transactionState: StateFlow<TransactionListUiState> = _transactionState.asStateFlow()

    // Asset state
    private val _assetState = MutableStateFlow<AssetListUiState>(AssetListUiState.Loading)
    val assetState: StateFlow<AssetListUiState> = _assetState.asStateFlow()

    // Operation state (for create/update/delete operations)
    private val _operationState = MutableStateFlow<OperationUiState>(OperationUiState.Idle)
    val operationState: StateFlow<OperationUiState> = _operationState.asStateFlow()

    // Assets by portfolio state
    private val _transactionByPortfolioState =
        MutableStateFlow<TransactionListUiState>(TransactionListUiState.Loading)
    val assetsByPortfolioState: StateFlow<TransactionListUiState> =
        _transactionByPortfolioState.asStateFlow()

    init {
        createMainPortfolioIfNeeded()
        loadInitialData()
    }

    fun loadTransactinsByPortfolio(portfolioId: String) {
        viewModelScope.launch {
            _transactionByPortfolioState.value = TransactionListUiState.Loading

            repository.getTransactionsByPortfolio(portfolioId).collect { result ->
                _transactionByPortfolioState.value = when (result) {
                    is Async.Loading -> TransactionListUiState.Loading
                    is Async.Error -> TransactionListUiState.Error(result.message)
                    is Async.Success -> TransactionListUiState.Success(result.data)
                }
            }
        }
    }

    // Load all data
    fun loadInitialData() {
        viewModelScope.launch {
            delay(1500)
            loadPortfolios()
            loadTransactions()
            loadAssets()
        }
    }

    private fun loadPortfolios() {
        viewModelScope.launch {
            repository.getAllPortfolios().collect { result ->
                when (result) {
                    is Async.Loading -> _portfolioState.value = PortfolioListUiState.Loading
                    is Async.Error -> _portfolioState.value =
                        PortfolioListUiState.Error(result.message)

                    is Async.Success -> {
                        _portfolioState.value = PortfolioListUiState.Success(result.data)

                        // Automatically create main portfolio if list is empty
                        if (result.data.isEmpty()) {
                            createMainPortfolio()
                        }
                    }
                }
            }
        }
    }

    fun loadTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions().collect { result ->
                _transactionState.value = when (result) {
                    is Async.Loading -> TransactionListUiState.Loading
                    is Async.Error -> TransactionListUiState.Error(result.message)
                    is Async.Success -> {
                        TransactionListUiState.Success(result.data)
                    }
                }
            }
        }
    }

    fun loadAssets() {
        viewModelScope.launch {
            repository.getAllAssets().collect { result ->
                _assetState.value = when (result) {
                    is Async.Loading -> AssetListUiState.Loading
                    is Async.Error -> AssetListUiState.Error(result.message)
                    is Async.Success -> AssetListUiState.Success(result.data)
                }
            }
        }
    }

    // Create main portfolio if it doesn't exist
    private fun createMainPortfolioIfNeeded() {
        viewModelScope.launch {
            val currentState = _portfolioState.value
            if (currentState is PortfolioListUiState.Success && currentState.portfolios.isEmpty()) {
                createMainPortfolio()
            }
        }
    }

    // Create main portfolio (default portfolio)
    private fun createMainPortfolio() {
        val mainPortfolio = Portfolio(
            id = "5e7d6795-8f40-4130-9f4a-e5dc0afe9957",
            name = "Default Portfolio",
            brokerBank = null, // User can set this later
            baseCurrency = "USD",
            description = "Default investment portfolio",
            createdAt = Clock.System.now().toEpochMilliseconds(),
        )
        createPortfolio(mainPortfolio)
    }

    // Create portfolio
    fun createPortfolio(portfolio: Portfolio) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading

            repository.insertPortfolio(portfolio).collect { result ->
                when (result) {
                    is Async.Loading -> {
                        // Already handled above
                    }

                    is Async.Error -> {
                        _operationState.value =
                            OperationUiState.Error("Failed to create portfolio: ${result.message}")
                        Napier.e("Failed to create portfolio: ${result.message}")
                    }

                    is Async.Success -> {
                        _operationState.value =
                            OperationUiState.Success("Portfolio created successfully")
                        loadPortfolios() // Refresh the list
                        Napier.d("Portfolio created successfully: ${portfolio.name}")
                    }
                }
            }
        }
    }

    fun createAsset(asset: Asset) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading

            repository.insertAsset(asset).collect { result ->
                when (result) {
                    is Async.Loading -> {
                        // Already handled above
                    }

                    is Async.Error -> {
                        _operationState.value =
                            OperationUiState.Error("Failed to create asset: ${result.message}")
                        Napier.e("❌ Failed to create asset: ${result.message}")
                    }

                    is Async.Success -> {
                        _operationState.value =
                            OperationUiState.Success("Asset created successfully")
                        loadAssets() // Refresh
                        Napier.d("✅ Asset created successfully: ${asset.name}")
                    }
                }
            }
        }
    }

    fun removePortfolio(portfolioId: String) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading

            repository.removePortfolio(portfolioId).collect { result ->
                when (result) {
                    is Async.Loading -> {
                        // Already handled above
                    }

                    is Async.Error -> {
                        _operationState.value =
                            OperationUiState.Error("Failed to remove portfolio: ${result.message}")
                        Napier.e("Failed to remove portfolio: ${result.message}")
                    }

                    is Async.Success -> {
                        _operationState.value =
                            OperationUiState.Success("Portfolio removed successfully")
                        loadPortfolios() // Refresh
                        loadTransactions() // Refresh
                        Napier.d("Portfolio removed successfully: $portfolioId")
                    }
                }
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading

            repository.insertTransaction(transaction).collect { result ->
                when (result) {
                    is Async.Loading -> {
                        // Already handled above
                    }

                    is Async.Error -> {
                        _operationState.value =
                            OperationUiState.Error("Failed to add transaction: ${result.message}")
                        Napier.e("Failed to add transaction: ${result.message}")
                    }

                    is Async.Success -> {
                        _operationState.value =
                            OperationUiState.Success("Transaction added successfully")
                        loadTransactions() // Refresh the list
                        loadPortfolios() // Refresh portfolios as values might change
                        Napier.d("Transaction added successfully: ${transaction.id}")
                    }
                }
            }
        }
    }

    fun removeTransaction(transactionId: String) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading

            repository.removeTransaction(transactionId).collect { result ->
                when (result) {
                    is Async.Loading -> {
                        // Already handled above
                    }

                    is Async.Error -> {
                        _operationState.value =
                            OperationUiState.Error("Failed to remove transaction: ${result.message}")
                        Napier.e("Failed to remove transaction: ${result.message}")
                    }

                    is Async.Success -> {
                        _operationState.value =
                            OperationUiState.Success("Transaction removed successfully")
                        loadTransactions() // Refresh the list
                        loadPortfolios() // Refresh portfolios as values might change
                        Napier.d("Transaction removed successfully: $transactionId")
                    }
                }
            }
        }
    }

    // Clear operation state
    fun clearOperationState() {
        _operationState.value = OperationUiState.Idle
    }

    fun getPortfolioById(portfolioId: String): Portfolio? {
        return when (val state = _portfolioState.value) {
            is PortfolioListUiState.Success -> {
                state.portfolios.find { it.id == portfolioId }
            }

            else -> null
        }
    }
}