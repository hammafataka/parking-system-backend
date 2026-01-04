package dev.mfataka.parkingsystembackend.resources

import dev.mfataka.parkingsystembackend.collection.Reservation
import dev.mfataka.parkingsystembackend.model.slot.ReserveSlotRequest
import dev.mfataka.parkingsystembackend.service.ReservationService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 27.12.2025 18:07
 */

@RestController
@RequestMapping("/api/v1/reservations")
class ReservationResource(
    private val reservationService: ReservationService
) {

    @PostMapping("/create")
    fun create(@RequestBody req: ReserveSlotRequest, auth: Authentication): Mono<Reservation> {
        return reservationService.create(req, auth.name)
    }

    @GetMapping("/my")
    fun my(auth: Authentication): Mono<List<Reservation>> {
        return reservationService.myReservations(auth.name)
    }
}