package com.example.buddy_backend.sell_order

import com.example.buddy_backend.config.CurrencyEnum
import com.example.buddy_backend.token.TokenEnum

data class SellOrderDto(
    val cryptoAmount: Double,
    val price: Double,
    val cryptoType: TokenEnum,
    val fiatType: CurrencyEnum,
    val owner: String,
    val status: SellOrderStatus
)
