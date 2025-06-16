package com.finblue.cache

import app.cash.sqldelight.Query
import com.finblue.db.AppDatabase
import com.finblue.db.SelectAssetsByPortfolio
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

    internal fun insertAsset(asset: Asset) {
        dbQuery.transaction {
            // Insert into main assets table
            dbQuery.insertAsset(
                asset_id = asset.id,
                portfolio_id = asset.portfolioId,
                symbol = asset.symbol,
                name = asset.name,
                asset_type = asset.assetType,
                created_at = asset.createdAt
            )

            // Insert into specific asset type table using sealed class pattern matching
            when (asset) {
                is Asset.Stock -> {
                    dbQuery.insertStock(
                        asset_id = asset.id,
                        exchange = asset.exchange
                    )
                }
                is Asset.Crypto -> {
                    dbQuery.insertCrypto(
                        asset_id = asset.id,
                        blockchain = asset.blockchain
                    )
                }
                is Asset.Fiat -> {
                    dbQuery.insertFiat(
                        asset_id = asset.id,
                        country = asset.country
                    )
                }
                is Asset.Other -> {
                    dbQuery.insertOther(
                        asset_id = asset.id,
                        category = asset.category,
                        condition_description = asset.conditionDescription,
                        description = asset.description
                    )
                }
            }
        }
    }

    internal fun removeAsset(assetId: String) {
        dbQuery.removeAsset(assetId)
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

    internal fun clearAndCreateAssets(assets: List<Asset>) {
        dbQuery.transaction {
            assets.forEach { asset ->
                insertAsset(asset)
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

    internal fun getAssetsByPortfolio(portfolioId: String): List<Asset> {
        return dbQuery.selectAssetsByPortfolio(portfolioId)
            .executeAsList()
            .map { it.toDomainAsset() }
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
            // For basic retrieval, return a BasicAsset or determine specific type
            // You might need additional queries to get type-specific data
            when (dbAsset.asset_type) {
                "stock" -> {
                    // You'd need to query the stocks table for exchange info
                    // For now, returning basic asset - you can enhance this later
                    Asset.Stock(
                        id = dbAsset.asset_id,
                        portfolioId = dbAsset.portfolio_id,
                        symbol = dbAsset.symbol,
                        name = dbAsset.name,
                        createdAt = dbAsset.created_at,
                        exchange = "UNKNOWN" // TODO: Query stocks table
                    )
                }
                "crypto" -> {
                    Asset.Crypto(
                        id = dbAsset.asset_id,
                        portfolioId = dbAsset.portfolio_id,
                        symbol = dbAsset.symbol,
                        name = dbAsset.name,
                        createdAt = dbAsset.created_at,
                        blockchain = "UNKNOWN" // TODO: Query crypto table
                    )
                }
                "fiat" -> {
                    Asset.Fiat(
                        id = dbAsset.asset_id,
                        portfolioId = dbAsset.portfolio_id,
                        symbol = dbAsset.symbol,
                        name = dbAsset.name,
                        createdAt = dbAsset.created_at,
                        country = "UNKNOWN" // TODO: Query fiat table
                    )
                }
                "other" -> {
                    Asset.Other(
                        id = dbAsset.asset_id,
                        portfolioId = dbAsset.portfolio_id,
                        symbol = dbAsset.symbol,
                        name = dbAsset.name,
                        createdAt = dbAsset.created_at,
                        category = "collectible" // TODO: Query others table
                    )
                }
                else -> {
                    // Fallback to Other type for unknown asset types
                    Asset.Other(
                        id = dbAsset.asset_id,
                        portfolioId = dbAsset.portfolio_id,
                        symbol = dbAsset.symbol,
                        name = dbAsset.name,
                        createdAt = dbAsset.created_at,
                        category = "collectible"
                    )
                }
            }
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

    private fun SelectAssetsByPortfolio.toDomainAsset(): Asset {
        return when (asset_type) {
            "stock" -> Asset.Stock(
                id = asset_id,
                portfolioId = portfolio_id,
                symbol = symbol,
                name = name,
                createdAt = created_at,
                exchange = exchange ?: throw IllegalStateException("Stock must have exchange")
            )
            "crypto" -> Asset.Crypto(
                id = asset_id,
                portfolioId = portfolio_id,
                symbol = symbol,
                name = name,
                createdAt = created_at,
                blockchain = blockchain ?: throw IllegalStateException("Crypto must have blockchain")
            )
            "fiat" -> Asset.Fiat(
                id = asset_id,
                portfolioId = portfolio_id,
                symbol = symbol,
                name = name,
                createdAt = created_at,
                country = country ?: throw IllegalStateException("Fiat must have country")
            )
            "other" -> Asset.Other(
                id = asset_id,
                portfolioId = portfolio_id,
                symbol = symbol,
                name = name,
                createdAt = created_at,
                category = category ?: throw IllegalStateException("Other must have category"),
                conditionDescription = condition_description,
                description = description
            )
            else -> throw IllegalArgumentException("Unknown asset type: $asset_type")
        }
    }
}

