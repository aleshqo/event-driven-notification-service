package com.example.banking.transaction.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "processed_requests")
data class ProcessedRequest(
    @Id
    val requestId: String,
    val processedAt: Instant = Instant.now()
)