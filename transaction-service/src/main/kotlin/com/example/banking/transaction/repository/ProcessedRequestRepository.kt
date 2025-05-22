package com.example.banking.transaction.repository

import com.example.banking.transaction.entity.ProcessedRequest
import org.springframework.data.jpa.repository.JpaRepository

interface ProcessedRequestRepository : JpaRepository<ProcessedRequest, String>
