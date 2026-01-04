package dev.mfataka.parkingsystembackend.repository

import dev.mfataka.parkingsystembackend.collection.Reservation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface ReservationRepository : ReactiveMongoRepository<Reservation, String> {
    fun existsBySlotNameAndStartDttmAndEndDttm(slotName: String, startDttm: LocalDateTime, endDttm: LocalDateTime): Mono<Boolean>
    fun findAllByUserNameOrderByStartDttmDesc(userName: String): Flux<Reservation>
}