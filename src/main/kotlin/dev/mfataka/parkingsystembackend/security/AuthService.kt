package dev.mfataka.parkingsystembackend.security

import dev.mfataka.parkingsystembackend.collection.RefreshToken
import dev.mfataka.parkingsystembackend.repository.RefreshTokenRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.empty
import reactor.core.publisher.Mono.fromCallable
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 21.12.2025 0:41
 */
@Service
class AuthService(
    private val authManager: ReactiveAuthenticationManager,
    private val jwtEncoder: JwtEncoder,
    private val refreshTokenRepository: RefreshTokenRepository) {
    private val accessTtlSeconds = 15L * 60L
    private val refreshTtlDays = 14L

    private val log = KotlinLogging.logger {}

    fun authenticate(request: Mono<LoginRequest>, exchange: ServerWebExchange): Mono<LoginResponse> {
        return request.flatMap {
            log.info { "loggin in user $it" }
            val token = UsernamePasswordAuthenticationToken(it.userName, it.password)
            authManager.authenticate(token)
        }.flatMap {
            val now = Instant.now()
            val userId = it.name

            val deviceId = UUID.randomUUID().toString()
            val refreshRaw = createRefreshRaw()
            val refreshHash = sha256Base64Url(refreshRaw)
            val refreshDoc = RefreshToken(
                userId = userId,
                deviceId = deviceId,
                tokenHash = refreshHash,
                expiresAt = now.plus(refreshTtlDays, ChronoUnit.DAYS)
            )

            refreshTokenRepository.save(refreshDoc)
                .then(fromCallable {
                    val claims = JwtClaimsSet.builder()
                        .subject(userId)
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(TOKEN_VALID_FOR_SECONDS))
                        .claim("roles", it.authorities)
                        .build()
                    val token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
                    setHttpOnlyCookie(exchange, REFRESH_TOKEN, refreshRaw, Duration.ofDays(refreshTtlDays))
                    setHttpOnlyCookie(exchange, DEVICE_ID, deviceId, Duration.ofDays(refreshTtlDays))
                    LoginResponse(token, accessTtlSeconds)
                })
        }
    }


    fun refreshToken(exchange: ServerWebExchange): Mono<LoginResponse> {
        val refreshRaw = (exchange.request.cookies.getFirst(REFRESH_TOKEN)
            ?.value
            ?: return Mono.error { ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token") })
        val deviceId = (exchange.request.cookies.getFirst(DEVICE_ID)
            ?.value
            ?: return Mono.error { ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid deviceId") })
        val presentedHash = sha256Base64Url(refreshRaw)
        val now = Instant.now()

        return refreshTokenRepository.findByTokenHash(presentedHash)
            .switchIfEmpty(Mono.error { ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token") })
            .flatMap {
                if (it.revokedAt != null || it.expiresAt.isBefore(now) || it.deviceId != deviceId) {
                    log.info { "refresh token was requested but did not pass check" }
                    return@flatMap Mono.error { ResponseStatusException(HttpStatus.UNAUTHORIZED) }
                }
                val newRefreshRaw = createRefreshRaw()
                val newHash = sha256Base64Url(newRefreshRaw)

                val revoked = it.copy(revokedAt = now, replacedByHash = newHash)
                val replacement = RefreshToken(userId = it.userId,
                                               deviceId = it.deviceId,
                                               tokenHash = newHash,
                                               expiresAt = now.plus(refreshTtlDays, ChronoUnit.DAYS)
                )

                refreshTokenRepository.save(revoked)
                    .then(refreshTokenRepository.save(replacement))
                    .then(fromCallable {
                        val claims = JwtClaimsSet.builder()
                            .subject(it.userId)
                            .issuedAt(now)
                            .expiresAt(now.plusSeconds(TOKEN_VALID_FOR_SECONDS))
                            .build()

                        val tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
                        setHttpOnlyCookie(exchange, "refresh_token", newRefreshRaw, Duration.ofDays(14))
                        setHttpOnlyCookie(exchange, "device_id", deviceId, Duration.ofDays(14))
                        LoginResponse(tokenValue, accessTtlSeconds)
                    })
            }
    }


    fun logout(exchange: ServerWebExchange): Mono<Void> {
        val refreshRaw = exchange.request.cookies.getFirst(REFRESH_TOKEN)?.value
        val deviceId = (exchange.request.cookies.getFirst(DEVICE_ID))?.value

        val now = Instant.now()

        val revokeMono = if (refreshRaw != null && deviceId != null) {
            val hash = sha256Base64Url(refreshRaw)
            refreshTokenRepository.findByTokenHash(hash)
                .flatMap { refreshTokenRepository.save(it.copy(revokedAt = now)).then() }
                .onErrorResume { empty() }
        } else {
            empty()
        }

        clearCookie(exchange, REFRESH_TOKEN)
        clearCookie(exchange, DEVICE_ID)

        return revokeMono

    }

    private fun clearCookie(exchange: ServerWebExchange, name: String) {
        exchange.response.apply {
            val cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth")
                .maxAge(Duration.ZERO)
                .build()
            addCookie(cookie)
        }

    }


    private fun setHttpOnlyCookie(exchange: ServerWebExchange, name: String, value: String, maxAge: Duration) {
        exchange.response.apply {
            addCookie(
                ResponseCookie.from(name, value)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/api/v1/auth")
                    .maxAge(maxAge)
                    .build()
            )
        }
    }

    private fun createRefreshRaw(): String = UUID.randomUUID().toString() + "." + UUID.randomUUID().toString()


    private fun sha256Base64Url(value: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(value.toByteArray(StandardCharsets.UTF_8))
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }
}