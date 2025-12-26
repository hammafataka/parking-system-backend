package dev.mfataka.parkingsystembackend.repository

import dev.mfataka.parkingsystembackend.collection.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:37
 */
@Repository
interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByUserNameAndPasswd(password: String, username: String): Mono<User>
    fun findByUserName(username: String): Mono<User>
}