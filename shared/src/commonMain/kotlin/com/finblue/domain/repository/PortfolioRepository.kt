package com.finblue.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.finblue.cache.Database
import com.finblue.domain.model.Portfolio
import com.finblue.domain.model.Asset
import com.finblue.domain.model.Transaction
import com.finblue.utils.Async
import io.github.aakira.napier.Napier


interface PortfolioRepositoryInterface {
    fun getAllPortfolios(): Flow<Async<List<Portfolio>>>
    fun getAllAssets(): Flow<Async<List<Asset>>>
    fun getAllTransactions(): Flow<Async<List<Transaction>>>
    fun getAssetsByPortfolio(portfolioId: String): Flow<Async<List<Asset>>>

    suspend fun insertPortfolio(portfolio: Portfolio): Flow<Async<Unit>>
    suspend fun removePortfolio(portfolioId: String): Flow<Async<Unit>>

    suspend fun insertAsset(asset: Asset): Flow<Async<Unit>>
    suspend fun removeAsset(assetId: String): Flow<Async<Unit>>

    suspend fun insertTransaction(transaction: Transaction): Flow<Async<Unit>>
    suspend fun removeTransaction(transactionId: String): Flow<Async<Unit>>

    suspend fun clearAndCreatePortfolios(portfolios: List<Portfolio>): Flow<Async<Unit>>
    suspend fun clearAndCreateAssets(assets: List<Asset>): Flow<Async<Unit>>
    suspend fun clearAndCreateTransactions(transactions: List<Transaction>): Flow<Async<Unit>>
}

class PortfolioRepository(
    private val database: Database,
    private val ioDispatcher: CoroutineDispatcher
) : PortfolioRepositoryInterface {

    override fun getAllPortfolios(): Flow<Async<List<Portfolio>>> = flow {
        emit(Async.Loading)
        try {
            val portfolios = database.getAllPortfolios()
            Napier.d("\uD83D\uDFE9\uD83D\uDFE9 Successfully retrieved ${portfolios.size} portfolios")
            emit(Async.Success(portfolios))
        } catch (e: Exception) {
            Napier.e("Failed to get portfolios", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while fetching portfolios"))
        }
    }.flowOn(ioDispatcher)

    override fun getAllAssets(): Flow<Async<List<Asset>>> = flow {
        emit(Async.Loading)
        try {
            val assets = database.getAllAssets()
            Napier.d("Successfully retrieved ${assets.size} assets")
            emit(Async.Success(assets))
        } catch (e: Exception) {
            Napier.e("Failed to get assets", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while fetching assets"))
        }
    }.flowOn(ioDispatcher)

    override fun getAllTransactions(): Flow<Async<List<Transaction>>> = flow {
        emit(Async.Loading)
        try {
            val transactions = database.getAllTransactions()
            Napier.d("Successfully retrieved ${transactions.size} transactions")
            emit(Async.Success(transactions))
        } catch (e: Exception) {
            Napier.e("Failed to get transactions", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while fetching transactions"))
        }
    }.flowOn(ioDispatcher)

    override fun getAssetsByPortfolio(portfolioId: String): Flow<Async<List<Asset>>> = flow {
        emit(Async.Loading)
        try {
            val assets = database.getAssetsByPortfolio(portfolioId)
            Napier.d("\uD83D\uDFE9\uD83D\uDFE9 Successfully retrieved ${assets.size} assets for portfolio: $portfolioId")
            emit(Async.Success(assets))
        } catch (e: Exception) {
            Napier.e("Failed to get assets for portfolio: $portfolioId", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while fetching portfolio assets"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun insertPortfolio(portfolio: Portfolio): Flow<Async<Unit>> = flow {
        emit(Async.Loading)
        try {
            database.insertPortfolio(portfolio)
            Napier.d("Successfully inserted portfolio: ${portfolio.id}")
            emit(Async.Success(Unit))
        } catch (e: Exception) {
            Napier.e("Failed to insert portfolio: ${portfolio.id}", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while inserting portfolio"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun removePortfolio(portfolioId: String): Flow<Async<Unit>> = flow {
        emit(Async.Loading)
        try {
            database.removePortfolio(portfolioId)
            Napier.d("Successfully removed portfolio: $portfolioId")
            emit(Async.Success(Unit))
        } catch (e: Exception) {
            Napier.e("Failed to remove portfolio: $portfolioId", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while removing portfolio"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun insertAsset(asset: Asset): Flow<Async<Unit>> = flow {
        emit(Async.Loading)
        try {
            database.insertAsset(asset)
            val assetType = when (asset) {
                is Asset.Stock -> "stock (${asset.exchange})"
                is Asset.Crypto -> "crypto (${asset.blockchain})"
                is Asset.Fiat -> "fiat (${asset.country})"
                is Asset.Other -> "other (${asset.category})"
            }
            Napier.d("\uD83D\uDFE9\uD83D\uDFE9 Successfully inserted $assetType asset: ${asset.name}")
            emit(Async.Success(Unit))
        } catch (e: Exception) {
            Napier.e("Failed to insert asset: ${asset.name}", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while inserting asset"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun removeAsset(assetId: String): Flow<Async<Unit>> = flow {
        emit(Async.Loading)
        try {
            database.removeAsset(assetId)
            Napier.d("\uD83D\uDFE9\uD83D\uDFE9 Successfully removed asset: $assetId")
            emit(Async.Success(Unit))
        } catch (e: Exception) {
            Napier.e("Failed to remove asset: $assetId", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while removing asset"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun insertTransaction(transaction: Transaction): Flow<Async<Unit>> = flow {
        emit(Async.Loading)
        try {
            database.insertTransaction(transaction)
            Napier.d("Successfully inserted transaction: ${transaction.id}")
            emit(Async.Success(Unit))
        } catch (e: Exception) {
            Napier.e("Failed to insert transaction: ${transaction.id}", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while inserting transaction"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun removeTransaction(transactionId: String): Flow<Async<Unit>> = flow {
        emit(Async.Loading)
        try {
            database.removeTransaction(transactionId)
            Napier.d("Successfully removed transaction: $transactionId")
            emit(Async.Success(Unit))
        } catch (e: Exception) {
            Napier.e("Failed to remove transaction: $transactionId", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while removing transaction"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun clearAndCreatePortfolios(portfolios: List<Portfolio>): Flow<Async<Unit>> = flow {
        emit(Async.Loading)
        try {
            database.clearAndCreatePortfolios(portfolios)
            Napier.d("Successfully cleared and created ${portfolios.size} portfolios")
            emit(Async.Success(Unit))
        } catch (e: Exception) {
            Napier.e("Failed to clear and create portfolios", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while clearing and creating portfolios"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun clearAndCreateAssets(assets: List<Asset>): Flow<Async<Unit>> = flow {
        emit(Async.Loading)
        try {
            database.clearAndCreateAssets(assets)
            Napier.d("\uD83D\uDFE9\uD83D\uDFE9 Successfully cleared and created ${assets.size} assets")
            emit(Async.Success(Unit))
        } catch (e: Exception) {
            Napier.e("Failed to clear and create assets", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while clearing and creating assets"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun clearAndCreateTransactions(transactions: List<Transaction>): Flow<Async<Unit>> = flow {
        emit(Async.Loading)
        try {
            database.clearAndCreateTransactions(transactions)
            Napier.d("Successfully cleared and created ${transactions.size} transactions")
            emit(Async.Success(Unit))
        } catch (e: Exception) {
            Napier.e("Failed to clear and create transactions", e)
            emit(Async.Error(e.message ?: "Unknown error occurred while clearing and creating transactions"))
        }
    }.flowOn(ioDispatcher)
}