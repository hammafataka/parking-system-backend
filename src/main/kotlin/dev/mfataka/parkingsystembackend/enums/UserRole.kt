package dev.mfataka.parkingsystembackend.enums

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:34
 */
enum class UserRole {
    EMPLOYEE,
    ADMIN;

    fun isEmployee() = this == EMPLOYEE
    fun isAdmin() = this == ADMIN
}