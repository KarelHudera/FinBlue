package com.finblue.cache

import app.cash.sqldelight.Query
import com.finblue.db.AppDatabase
import com.finblue.domain.model.Portfolio
import com.finblue.domain.model.Asset
import com.finblue.domain.model.Transaction
import com.finblue.db.Portfolios as PortfolioEntity
import com.finblue.db.Assets as AssetEntity
import com.finblue.db.Transactions as TransactionEntity

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun getAllPortfolios(): List<Portfolio> {
        return dbQuery.selectAllPortfolios().asDomainModelPortfolios()
    }

    internal fun insertPortfolio(portfolio: Portfolio) {
        dbQuery.insertPortfolio(
            portfolio_id = portfolio.id,
            name = portfolio.name,
            broker_bank = portfolio.brokerBank,
            base_currency = portfolio.baseCurrency,
            description = portfolio.description,
            created_at = portfolio.createdAt
        )
    }

    internal fun removePortfolio(portfolioId: String) {
        dbQuery.removePortfolio(portfolioId)
    }

    // Asset operations
    internal fun getAllAssets(): List<Asset> {
        return dbQuery.selectAllAssets().asDomainModelAssets()
    }

    // Transaction operations
    internal fun getAllTransactions(): List<Transaction> {
        return dbQuery.selectAllTransactions().asDomainModelTransactions()
    }

    internal fun insertTransaction(transaction: Transaction) {
        dbQuery.insertTransaction(
            transaction_id = transaction.id,
            portfolio_id = transaction.portfolioId,
            asset_id = transaction.assetId,
            type = transaction.type,
            quantity = transaction.quantity,
            price = transaction.price,
            currency = transaction.currency,
            notes = transaction.notes,
            transaction_date = transaction.transactionDate,
            created_at = transaction.createdAt
        )
    }

    internal fun removeTransaction(transactionId: String) {
        dbQuery.removeTransaction(transactionId)
    }

    // Bulk operations
    internal fun clearAndCreatePortfolios(portfolios: List<Portfolio>) {
        dbQuery.transaction {
            // Note: This will cascade delete assets and transactions
            portfolios.forEach { portfolio ->
                insertPortfolio(portfolio)
            }
        }
    }

    internal fun clearAndCreateTransactions(transactions: List<Transaction>) {
        dbQuery.transaction {
            transactions.forEach { transaction ->
                insertTransaction(transaction)
            }
        }
    }

    // Extension functions to convert database entities to domain models
    private fun Query<PortfolioEntity>.asDomainModelPortfolios(): List<Portfolio> {
        return executeAsList().map { dbPortfolio ->
            Portfolio(
                id = dbPortfolio.portfolio_id,
                name = dbPortfolio.name,
                brokerBank = dbPortfolio.broker_bank,
                baseCurrency = dbPortfolio.base_currency,
                description = dbPortfolio.description,
                createdAt = dbPortfolio.created_at,
            )
        }
    }

    private fun Query<AssetEntity>.asDomainModelAssets(): List<Asset> {
        return executeAsList().map { dbAsset ->
            Asset(
                id = dbAsset.asset_id,
                portfolioId = dbAsset.portfolio_id,
                symbol = dbAsset.symbol,
                name = dbAsset.name,
                assetType = dbAsset.asset_type,
                createdAt = dbAsset.created_at
            )
        }
    }

    private fun Query<TransactionEntity>.asDomainModelTransactions(): List<Transaction> {
        return executeAsList().map { dbTransaction ->
            Transaction(
                id = dbTransaction.transaction_id,
                portfolioId = dbTransaction.portfolio_id,
                assetId = dbTransaction.asset_id,
                type = dbTransaction.type,
                quantity = dbTransaction.quantity,
                price = dbTransaction.price,
                currency = dbTransaction.currency,
                notes = dbTransaction.notes,
                transactionDate = dbTransaction.transaction_date,
                createdAt = dbTransaction.created_at
            )
        }
    }
}