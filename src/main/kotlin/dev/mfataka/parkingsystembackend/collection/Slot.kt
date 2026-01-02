package dev.mfataka.parkingsystembackend.collection

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.LocalDateTime.now

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:30
 */
@Document
data class Slot(
    @Id val id: String? = null,
    @Indexed(unique = true)
    val name: String,
    val garageName: String,
    val reservedBy: String? = null,
    val reservedFrom: LocalDateTime? = null,
    val reservedUntil: LocalDateTime? = null,
    val lastModified: LocalDateTime = now()
) {
    fun isAvailable(toReserveFrom: LocalDateTime): Boolean =
        reservedUntil?.isBefore(toReserveFrom) ?: false

    fun toDto() = SlotDto(id ?: error("id not found"),
                          name,
                          garageName,
                          isAvailable(now()),
                          reservedBy,
                          reservedFrom,
                          reservedUntil)
}


data class SlotDto(val id: String,
                   val name: String,
                   val garageName: String,
                   val available: Boolean,
                   val reservedBy: String? = null,
                   val reservedFrom: LocalDateTime? = null,
                   val reservedUntil: LocalDateTime? = null)