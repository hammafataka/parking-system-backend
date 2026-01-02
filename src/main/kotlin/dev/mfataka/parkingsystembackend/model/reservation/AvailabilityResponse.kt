package dev.mfataka.parkingsystembackend.model.reservation

import dev.mfataka.parkingsystembackend.collection.SlotDto

data class AvailabilityResponse(
    val date: String,
    val availableSlots: List<SlotDto>,
    val reservedSlots: List<SlotDto>
)