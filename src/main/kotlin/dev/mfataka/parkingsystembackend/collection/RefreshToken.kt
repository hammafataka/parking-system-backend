package dev.mfataka.parkingsystembackend.collection

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 21.12.2025 2:18
 */
@Document("refresh_token")
data class RefreshToken(
    @Id val id: String? = null,
    val userId: String,
    val deviceId: String,
    val tokenHash: String,
    val expiresAt: Instant,
    val revokedAt: Instant? = null,
    val replacedByHash: String? = null,
    val createdAt: Instant = Instant.now()
)


