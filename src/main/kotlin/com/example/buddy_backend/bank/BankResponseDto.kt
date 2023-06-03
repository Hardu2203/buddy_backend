package com.example.buddy_backend.bank

import java.security.PublicKey

data class BankResponseDto(
        val publicKey: String,
        val bank: BankType,
        val branchCode: String,
        val accountType: AccountType,
        val accountNumber: String
)