package dev.mfataka.parkingsystembackend.model.slot

import java.time.LocalDateTime

data class ReserveSlotRequest(
    val slotName: String,
    val reservedBy: String,
    val reservedFrom: LocalDateTime,
    val reservedUntil: LocalDateTime
)