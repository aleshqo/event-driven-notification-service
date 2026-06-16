package com.aleshqo.banking.transaction.outbox

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "outbox_events")
class OutboxEvent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 128)
    var topic: String,

    @Column(nullable = false, length = 64)
    var messageKey: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var payload: String,

    @Column(nullable = false)
    var published: Boolean = false,

    var createdAt: Instant = Instant.now(),

    var publishedAt: Instant? = null
)
