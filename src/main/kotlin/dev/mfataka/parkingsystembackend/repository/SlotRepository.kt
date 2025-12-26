package dev.mfataka.parkingsystembackend.repository

import dev.mfataka.parkingsystembackend.collection.Slot
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:36
 */
@Repository
interface SlotRepository : MongoRepository<Slot, String> {

    fun findSlotsByAvailableIsTrue(): List<Slot>
    fun existsByName(name: String): Boolean
    fun findByName(slotName: String): Slot?
    fun findAllByGarageName(garageName: String): List<Slot>
}