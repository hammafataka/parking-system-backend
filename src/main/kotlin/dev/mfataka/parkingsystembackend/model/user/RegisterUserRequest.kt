package dev.mfataka.parkingsystembackend.model.user

import dev.mfataka.parkingsystembackend.collection.User

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 19.12.2025 0:41
 */
data class RegisterUserRequest(
    val name: String,
    val reservedSlot: String,
    val username: String,
    val password: String) {

    fun asUser() = User(name = name, reservedSlot = reservedSlot, userName = username, passwd = password)
}