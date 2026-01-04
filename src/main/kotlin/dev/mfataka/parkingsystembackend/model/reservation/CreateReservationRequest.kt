package dev.mfataka.parkingsystembackend.model.reservation

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 27.12.2025 18:07
 */

data class CreateReservationRequest(
    val slotName: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    val startDttm: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    val endDttm: LocalDateTime,
)