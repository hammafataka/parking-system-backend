package dev.mfataka.parkingsystembackend.service

import dev.mfataka.parkingsystembackend.collection.Slot
import dev.mfataka.parkingsystembackend.model.reservation.SlotAvailabilityRequest
import dev.mfataka.parkingsystembackend.model.reservation.SlotAvailabilityResponse
import dev.mfataka.parkingsystembackend.model.slot.ReserveSlotRequest
import dev.mfataka.parkingsystembackend.model.slot.SlotRequest
import dev.mfataka.parkingsystembackend.repository.SlotRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:37
 */

@Service
class SlotService(@Autowired private val slotRepository: SlotRepository,
                  private val mongoTemplate: ReactiveMongoTemplate) {


    fun findAllByGarageName(garageName: String): Flux<Slot> {
        return slotRepository.findAllByGarageName(garageName)
    }

    fun reserve(req: ReserveSlotRequest, username: String): Mono<Slot> {
        return slotRepository.findByName(req.slotName)
            .switchIfEmpty(Mono.error { error("slot ${req.slotName} not found") })
            .flatMap {
                if (!it.isAvailable(req.reservedFrom)) {
                    throw IllegalStateException("Slot ${req.slotName} is not available")
                }
                val updated = it.copy(
                    reservedBy = username,
                    reservedFrom = req.reservedFrom,
                    reservedUntil = req.reservedUntil,
                    lastModified = LocalDateTime.now()
                )

                slotRepository.save(updated)
            }
    }

    fun findByName(name: String): Mono<Slot> {
        return slotRepository.findByName(name)
    }

    fun findById(id: String) = slotRepository.findById(id)

    fun findAll() = slotRepository.findAll()
        .map { slot -> slot.toDto() }

    fun registerSlot(requestSlot: SlotRequest): Mono<Slot> {
        return slotRepository.existsByName(requestSlot.name)
            .flatMap { exists ->
                if (exists) {
                    error("A slot's id ${requestSlot.name} already exists.")
                }
                slotRepository.save(requestSlot.asSlot())
            }


    }

    fun distinctGarages(): Flux<String> {
        return mongoTemplate.query<Slot>()
            .distinct("garageName")
            .`as`(String::class.java)
            .all()
    }

    fun slotsForRange(request: SlotAvailabilityRequest): Mono<SlotAvailabilityResponse> {
        val query = Query().addCriteria(
            Criteria().orOperator(
                Criteria.where("reservedUntil").lte(request.startDttm),
                Criteria.where("reservedFrom").gte(request.endDttm)
            )
        )
        return mongoTemplate.find<Slot>(query)
            .map { it.toDto() }
            .collectList()
            .map {
                SlotAvailabilityResponse(request.startDttm, request.endDttm, it)
            }
    }

}