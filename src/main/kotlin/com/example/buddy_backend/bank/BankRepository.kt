package com.example.buddy_backend.bank;

import org.springframework.data.jpa.repository.JpaRepository

interface BankRepository : JpaRepository<Bank, String> {
}