package dev.mfataka.parkingsystembackend.model.reservation

import dev.mfataka.parkingsystembackend.collection.SlotDto
import java.time.LocalDateTime

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 03.01.2026 21:08
 */
data class SlotAvailabilityRequest(val startDttm: LocalDateTime, val endDttm: LocalDateTime)
data class SlotAvailabilityResponse(
    val startDttm: LocalDateTime, val endDttm: LocalDateTime,
    val availableSlots: List<SlotDto>
)
