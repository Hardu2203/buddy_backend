package com.example.buddy_backend.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "buddy_user")
class User(

        @Id
        @Column(name = "public_key")
        val publicKey: String,

        var nonce: Long,

        val name: String? = null,

        val email: String? = null,
) {

}