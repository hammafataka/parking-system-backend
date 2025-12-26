package dev.mfataka.parkingsystembackend.resources

import dev.mfataka.parkingsystembackend.security.AuthService
import dev.mfataka.parkingsystembackend.security.LoginRequest
import dev.mfataka.parkingsystembackend.security.LoginResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 21.12.2025 2:34
 */
@RestController
@RequestMapping("/api/v1/auth")
class AuthResource(private val authService: AuthService) {


    @PostMapping("/login")
    fun login(@RequestBody request: Mono<LoginRequest>, exchange: ServerWebExchange): Mono<LoginResponse> {
        return authService.authenticate(request, exchange)
    }

    @PostMapping("/refresh")
    fun refreshToken(exchange: ServerWebExchange): Mono<LoginResponse> {
        return authService.refreshToken(exchange)
    }

    @PostMapping("logout")
    fun logout(exchange: ServerWebExchange): Mono<Void> {
        return authService.logout(exchange)
    }


}