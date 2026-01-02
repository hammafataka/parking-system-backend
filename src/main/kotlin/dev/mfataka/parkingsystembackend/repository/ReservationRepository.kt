package dev.mfataka.parkingsystembackend.repository

import dev.mfataka.parkingsystembackend.collection.Reservation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

interface ReservationRepository : ReactiveMongoRepository<Reservation, String> {
    fun existsBySlotNameAndDate(slotName: String, date: LocalDate): Mono<Boolean>
    fun findAllByDate(date: LocalDate): Flux<Reservation>
    fun findAllByUserNameOrderByDateDesc(userName: String): Flux<Reservation>
}