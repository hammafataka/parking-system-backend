package dev.mfataka.parkingsystembackend.collection


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 27.12.2025 18:04
 */

@Document("reservations")
@CompoundIndex(name = "uniq_slot_date", def = "{'slotName': 1, 'date': 1}", unique = true)
data class Reservation(
    @Id val id: String? = null,
    val slotName: String,
    val garageName: String,
    val userName: String,
    val date: LocalDate,
    val createdAt: LocalDateTime = LocalDateTime.now()
)