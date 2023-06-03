package com.example.buddy_backend

class ControllerBase {

    companion object {
        private const val API_ROOT_URL = "/api"

        const val USER = "$API_ROOT_URL/user"

        const val BANK = "$API_ROOT_URL/bank"

        const val SELL_ORDER = "$API_ROOT_URL/sell-order"
    }

}