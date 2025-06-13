package com.finblue.domain.model

data class Asset(
    val id: String,
    val portfolioId: String,
    val symbol: String,
    val name: String,
    val assetType: String,
    val createdAt: Long
)