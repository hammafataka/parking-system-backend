package dev.mfataka.parkingsystembackend.repository

import dev.mfataka.parkingsystembackend.collection.Slot
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:36
 */
@Repository
interface SlotRepository : ReactiveMongoRepository<Slot, String> {

    fun existsByName(name: String): Mono<Boolean>
    fun findByName(slotName: String): Mono<Slot>
    fun findAllByGarageName(garageName: String): Flux<Slot>
}