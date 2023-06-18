package com.example.buddy_backend.sell_order;

import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigInteger

interface SellOrderRepository : JpaRepository<SellOrder, String> {


    fun findFirstByTokenAddressAndSellerPublicKeyAndAmountOrderBySellOrderNumberAsc(
        tokenAddress: String,
        sellerPublicKey: String,
        amount: BigInteger
    ): SellOrder


}