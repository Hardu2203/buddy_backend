package com.example.buddy_backend.chain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigInteger
import java.time.LocalDateTime

@Entity
@Table(name = "block_chain")
@EntityListeners(AuditingEntityListener::class)
class BlockChain(

    @Id
    @GeneratedValue
    val id:  Long = 0,

    var blockNumber: BigInteger,

) {
    @Column(name = "created_date", nullable = false)
    @CreatedDate
    var createdDate: LocalDateTime? = null

    @Column(name = "created_by")
    @CreatedBy
    var createdBy: String? = null

    @LastModifiedDate
    var lastModifiedDate: LocalDateTime? = null

    @LastModifiedBy
    var lastModifiedBy: String? = null
}