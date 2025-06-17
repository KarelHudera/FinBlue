package com.finblue.domain.model

data class Portfolio(
    val id: String,
    val name: String,
    val brokerBank: String?,
    val baseCurrency: String,
    val description: String?,
    val createdAt: Long,
)