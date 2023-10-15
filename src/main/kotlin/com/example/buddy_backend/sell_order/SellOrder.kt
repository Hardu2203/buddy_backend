package com.example.buddy_backend.sell_order

import com.example.buddy_backend.config.CurrencyEnum
import com.example.buddy_backend.token.TokenEnum
import jakarta.persistence.*
import java.math.BigInteger

@Entity
class SellOrder(

    @Id
    val id: BigInteger,

    val sellerPublicKey: String,

    val tokenAddress: String,

    var status: SellOrderStatus,

    val amount: BigInteger,

    @Enumerated(EnumType.STRING)
    val currency: CurrencyEnum,

    val price: BigInteger,

    val token: TokenEnum,

    var buyerPublicKey: String? = null,
) {

    fun toSellOrderDto(): SellOrderDto {
        return SellOrderDto(
            cryptoAmount = amount.toDouble(),
            price = price.toDouble(),
            cryptoType = token,
            fiatType = currency,
            owner = sellerPublicKey,
            status = status
        )
    }

}

enum class SellOrderStatus {
    CREATED,
    ALLOWANCE_CHECK_FAILED,
    TRANSFERING_FUNDS_TO_BUDDY_WALLET,
    ACTIVE,
    WAITING_BUYER_PAYMENT,
    COMPLETED,
    CANCELLED,
}
