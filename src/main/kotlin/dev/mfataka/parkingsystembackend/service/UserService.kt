package dev.mfataka.parkingsystembackend.service

import dev.mfataka.parkingsystembackend.collection.User
import dev.mfataka.parkingsystembackend.model.user.RegisterUserRequest
import dev.mfataka.parkingsystembackend.repository.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:41
 */
@Service
class UserService(@Autowired val userRepository: UserRepository) {

    private val log = KotlinLogging.logger {}

    fun findById(id: String) = userRepository.findById(id)
    fun findByUserName(userName: String) = userRepository.findByUserName(userName)

    fun findAll() = userRepository.findAll()

    fun registerOne(request: RegisterUserRequest): Mono<User> {
        return userRepository.save(request.asUser())
            .doOnError {
                log.error { it }
            }
    }
}