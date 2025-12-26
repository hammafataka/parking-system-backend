package dev.mfataka.parkingsystembackend.service

import dev.mfataka.parkingsystembackend.collection.Slot
import dev.mfataka.parkingsystembackend.model.slot.ReserveSlotRequest
import dev.mfataka.parkingsystembackend.model.slot.SlotRequest
import dev.mfataka.parkingsystembackend.repository.SlotRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:37
 */

@Service
class SlotService(@Autowired val slotRepository: SlotRepository) {


    fun findAllByGarageName(garageName: String): List<Slot> {
        return slotRepository.findAllByGarageName(garageName)
    }

    fun reserve(req: ReserveSlotRequest): Slot {
        val slot = slotRepository.findByName(req.slotName)
            ?: throw IllegalArgumentException("Slot not found: ${req.slotName}")

        if (!slot.available) {
            throw IllegalStateException("Slot ${req.slotName} is not available")
        }

        val updated = slot.copy(
            available = false,
            reservedBy = req.reservedBy,
            reservedFrom = req.reservedFrom,
            reservedUntil = req.reservedUntil,
            lastModified = LocalDateTime.now()
        )

        return slotRepository.save(updated)
    }

    fun findAllAvailable() = slotRepository.findSlotsByAvailableIsTrue()

    fun findById(id: String) = slotRepository.findById(id)

    fun findAll() = slotRepository.findAll()

    fun registerSlot(requestSlot: SlotRequest): Slot {
        val exists = slotRepository.existsByName(requestSlot.name)
        if (exists) {
            error("A slot's id ${requestSlot.name} already exists.")
        }
        return slotRepository.save(requestSlot.asSlot())

    }
}