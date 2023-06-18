package com.example.buddy_backend.chain;

import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

interface BlockChainRepository : JpaRepository<BlockChain, Long> {


    fun findTopByOrderByIdDesc(): BlockChain?


}