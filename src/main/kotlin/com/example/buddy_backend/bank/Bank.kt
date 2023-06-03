package com.example.buddy_backend.bank

import com.example.buddy_backend.user.User
import jakarta.persistence.*

//@Entity
//class Bank(
//
//        @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
//        @JoinColumn
//        @MapsId
//        val user: User,
//
//        val bank: BankType,
//
//        val branchCode: String,
//
//        val accountType: AccountType,
//
//        val accountNumber: String
//) {
//    @Id
//    @Column(name = "public_key")
//    var publicKey: String? = null
//}

@Entity
class Bank(
        @OneToOne
        @JoinColumn
        @MapsId
        val user: User,

        val bank: BankType,

        val branchCode: String,

        val accountType: AccountType,

        var accountNumber: String
) {
    @Id
    @Column(name = "public_key")
    var publicKey: String? = null
}


enum class AccountType {
    CHEQUE,
    SAVINGS
}

enum class BankType {
    ABSA,
    FNB,
    STANDARD_BANK,
    DISCOVERY,
    NEDBANK
}

