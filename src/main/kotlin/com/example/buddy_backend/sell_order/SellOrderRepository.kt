package com.example.buddy_backend.sell_order;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigInteger

interface SellOrderRepository : JpaRepository<SellOrder, BigInteger> {


    @Query("select (count(s) > 0) from SellOrder s where s.id = ?1 and s.buyerPublicKey is not null ")
    fun sellOrderIsTransferred(id: BigInteger): Boolean


    @Query("select s " +
            "from SellOrder s " +
            "where s.sellerPublicKey = :sellerPublicKey " +
            "and s.status = com.example.buddy_backend.sell_order.SellOrderStatus.ACTIVE")
    fun findBySellerPublicKey(sellerPublicKey: String): Set<SellOrder>


}
