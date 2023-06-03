package com.example.buddy_backend.sell_order

import com.example.buddy_backend.config.BlockchainConfig
import org.springframework.stereotype.Service

@Service
class SellOrderService(
    private val blockchainConfig: BlockchainConfig
) {

    fun createSellOrder() {

    }

    private fun checkAllowance(): Boolean {
        return true
    }

}