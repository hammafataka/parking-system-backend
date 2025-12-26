package dev.mfataka.parkingsystembackend.model.slot

data class ReserveSlotResponse(
    val slotName: String,
    val reservedBy: String,
    val available: Boolean
)