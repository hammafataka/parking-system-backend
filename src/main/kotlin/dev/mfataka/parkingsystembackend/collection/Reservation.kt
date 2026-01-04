package dev.mfataka.parkingsystembackend.collection


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 27.12.2025 18:04
 */

@Document("reservations")
@CompoundIndex(name = "idx_slot_start_end", def = "{'slotName': 1, 'startDttm': 1, 'endDttm': 1}")
data class Reservation(
    @Id val id: String? = null,
    val slotName: String,
    val garageName: String,
    val userName: String,
    val startDttm: LocalDateTime,
    val endDttm: LocalDateTime,
    val createdAt: LocalDateTime = LocalDateTime.now()
)