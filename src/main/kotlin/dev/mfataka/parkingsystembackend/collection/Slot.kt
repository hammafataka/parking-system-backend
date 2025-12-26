package dev.mfataka.parkingsystembackend.collection

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

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
    val available: Boolean = true,
    val reservedBy: String? = null,
    val reservedFrom: LocalDateTime? = null,
    val reservedUntil: LocalDateTime? = null,
    val lastModified: LocalDateTime = LocalDateTime.now()
)
