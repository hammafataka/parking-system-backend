package dev.mfataka.parkingsystembackend.security

import dev.mfataka.parkingsystembackend.repository.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 21.12.2025 0:11
 */
@Service
class MongoUserDetailsService(val userRepository: UserRepository) : ReactiveUserDetailsService {

    @Suppress("unchecked_cast")
    override fun findByUsername(username: String): Mono<UserDetails> {
        return userRepository.findByUserName(username)
            .switchIfEmpty(Mono.error(UsernameNotFoundException("user not found: $username")))
                as Mono<UserDetails>
    }
}