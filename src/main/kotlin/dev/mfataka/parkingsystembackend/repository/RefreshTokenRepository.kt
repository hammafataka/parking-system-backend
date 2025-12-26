package dev.mfataka.parkingsystembackend.repository

import dev.mfataka.parkingsystembackend.collection.RefreshToken
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 21.12.2025 2:21
 */
interface RefreshTokenRepository : ReactiveMongoRepository<RefreshToken, String> {

    fun findByTokenHash(tokenHash: String): Mono<RefreshToken>

    fun findAllByUserIdAndDeviceIdAndRevokedAtIsNull(userId: String, deviceId: String): Flux<RefreshToken>
}