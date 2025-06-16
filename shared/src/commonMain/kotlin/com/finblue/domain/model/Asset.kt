package com.finblue.domain.model

sealed class Asset {
    abstract val id: String
    abstract val portfolioId: String
    abstract val symbol: String
    abstract val name: String
    abstract val assetType: String
    abstract val createdAt: Long

    data class Stock(
        override val id: String,
        override val portfolioId: String,
        override val symbol: String,
        override val name: String,
        override val createdAt: Long,
        val exchange: String,
        override val assetType: String = "stock"
    ) : Asset()

    data class Crypto(
        override val id: String,
        override val portfolioId: String,
        override val symbol: String,
        override val name: String,
        override val createdAt: Long,
        val blockchain: String,
        override val assetType: String = "crypto"
    ) : Asset()

    data class Fiat(
        override val id: String,
        override val portfolioId: String,
        override val symbol: String,
        override val name: String,
        override val createdAt: Long,
        val country: String,
        override val assetType: String = "fiat"
    ) : Asset()

    data class Other(
        override val id: String,
        override val portfolioId: String,
        override val symbol: String,
        override val name: String,
        override val createdAt: Long,
        val category: String, // 'real_estate', 'collectible', 'commodity'
        val conditionDescription: String? = null,
        val description: String? = null,
        override val assetType: String = "other"
    ) : Asset()
}

// Extension functions for easy type checking
fun Asset.isStock(): Boolean = this is Asset.Stock
fun Asset.isCrypto(): Boolean = this is Asset.Crypto
fun Asset.isFiat(): Boolean = this is Asset.Fiat
fun Asset.isOther(): Boolean = this is Asset.Other