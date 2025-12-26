package dev.mfataka.parkingsystembackend.security

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 21.12.2025 0:45
 */

data class LoginRequest(val userName: String, val password: String)


data class LoginResponse(val token: String, val expiresIn: Long)

data class UserResponse(val name: String, val reservedSlot: String, val role: String)