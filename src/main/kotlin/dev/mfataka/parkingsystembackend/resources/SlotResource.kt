package dev.mfataka.parkingsystembackend.resources

import dev.mfataka.parkingsystembackend.collection.Slot
import dev.mfataka.parkingsystembackend.collection.SlotDto
import dev.mfataka.parkingsystembackend.model.slot.ReserveSlotRequest
import dev.mfataka.parkingsystembackend.model.slot.SlotRequest
import dev.mfataka.parkingsystembackend.service.SlotService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:45
 */
@RestController
@RequestMapping(path = ["/api/v1/slots"])
class SlotResource(@Autowired val slotsService: SlotService) {

    @GetMapping(path = ["/garages"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun garages(): Mono<List<String>> {
        return slotsService.distinctGarages()
            .collectList()

    }

    @GetMapping(path = ["/all/{garageName}"])
    fun findAllByGarageName(@PathVariable garageName: String): Mono<List<Slot>> {
        return slotsService.findAllByGarageName(garageName)
            .collectList()
    }

    @PostMapping(path = ["/add"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addSlot(@RequestBody requestSlot: SlotRequest): Mono<Slot> {
        return slotsService.registerSlot(requestSlot)
    }

    @PostMapping("/reserve", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun reserveSlot(@RequestBody req: ReserveSlotRequest): Mono<Slot> {
        return slotsService.reserve(req)
    }

    @GetMapping(value = ["/all"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAllSlots(): Mono<List<SlotDto>> = slotsService.findAll()
        .collectList()

}