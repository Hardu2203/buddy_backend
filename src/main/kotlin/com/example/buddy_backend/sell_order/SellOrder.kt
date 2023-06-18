package com.example.buddy_backend.sell_order

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.math.BigDecimal
import java.math.BigInteger

@Entity
class SellOrder(

    @Id
    val txHash: String,

    val sellerPublicKey: String,

    val tokenAddress: String,

    var status: SellOrderStatus,

    val amount: BigInteger,

    var buyerPublicKey: String? = null,

    @GeneratedValue
    val sellOrderNumber: Long = 0,

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