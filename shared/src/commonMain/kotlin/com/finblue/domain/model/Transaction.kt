package com.finblue.domain.model

data class Transaction(
    val id: String,
    val portfolioId: String,
    val assetId: String,
    val type: String,
    val quantity: Double,
    val price: Double,
    val currency: String,
    val notes: String?,
    val transactionDate: Long,
    val createdAt: Long
)