package dev.mfataka.parkingsystembackend.model.slot

import dev.mfataka.parkingsystembackend.collection.Slot

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:56
 */
class SlotRequest(val name: String, val garageName: String) {
    fun asSlot() = Slot(name = name, garageName = garageName)
}