package dev.mfataka.parkingsystembackend.collection

import dev.mfataka.parkingsystembackend.enums.UserRole
import dev.mfataka.parkingsystembackend.security.UserResponse
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:32
 */
@Document
data class User(
    @Id val id: String? = null,
    @Indexed(unique = true)
    val name: String,
    val role: UserRole = UserRole.EMPLOYEE,
    val reservedSlot: String = "",
    @Indexed(unique = true)
    val userName: String,
    @Field(name = "password")
    val passwd: String
) : UserDetails {

    fun asResponse() = UserResponse(this.name, this.reservedSlot, this.role.name)
    override fun getAuthorities(): Collection<GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority(role.name))

    override fun getPassword() = passwd

    override fun getUsername() = userName
}
