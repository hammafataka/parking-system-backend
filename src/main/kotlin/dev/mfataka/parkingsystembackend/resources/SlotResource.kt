package dev.mfataka.parkingsystembackend.resources

import dev.mfataka.parkingsystembackend.collection.Slot
import dev.mfataka.parkingsystembackend.model.BaseResponse
import dev.mfataka.parkingsystembackend.model.BaseResponse.Companion.ok
import dev.mfataka.parkingsystembackend.model.slot.ReserveSlotRequest
import dev.mfataka.parkingsystembackend.model.slot.SlotRequest
import dev.mfataka.parkingsystembackend.service.SlotService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:45
 */
@RestController
@RequestMapping(path = ["/api/v1/slots"])
class SlotResource(@Autowired val slotsService: SlotService) {


    @GetMapping(path = ["/all/{garageName}"])
    fun findAllByGarageName(@PathVariable garageName: String): BaseResponse<List<Slot>> {
        return ok(slotsService.findAllByGarageName(garageName))
    }

    @PostMapping(path = ["/add"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addSlot(@RequestBody requestSlot: SlotRequest): BaseResponse<Slot> {
        return ok(slotsService.registerSlot(requestSlot))
    }

    @PostMapping("/reserve", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun reserveSlot(@RequestBody req: ReserveSlotRequest): BaseResponse<Slot> {
        return ok(slotsService.reserve(req))
    }

    @GetMapping(value = ["/all"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAllSlots() = ok(slotsService.findAll())


    @GetMapping(value = ["/all/available"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAllAvailableSlots() = ok(slotsService.findAllAvailable())

}