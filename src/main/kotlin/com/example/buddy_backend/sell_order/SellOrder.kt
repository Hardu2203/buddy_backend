package com.example.buddy_backend.sell_order

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
class SellOrder(

    @Id
    @GeneratedValue
    val id: Long = 0,

    val sellerPublicKey: String,

    val tokenAddress: String,

    val token: String,

    val status: SellOrderStatus,

    val buyerPublicKey: String?,

    val amount: BigDecimal

)

enum class SellOrderStatus{
    CREATED,
    ALLOWANCE_CHECK_FAILED,
    TRANSFERING_FUNDS_TO_BUDDY_WALLET,
    ACTIVE,
    WAITING_BUYER_PAYMENT,
    COMPLETED,
    CANCELLED,
}