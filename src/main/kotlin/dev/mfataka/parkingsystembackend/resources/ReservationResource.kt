package dev.mfataka.parkingsystembackend.resources

import dev.mfataka.parkingsystembackend.collection.Reservation
import dev.mfataka.parkingsystembackend.model.reservation.AvailabilityResponse
import dev.mfataka.parkingsystembackend.model.reservation.CreateReservationRequest
import dev.mfataka.parkingsystembackend.service.ReservationService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.time.LocalDate


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

    @GetMapping("/availability")
    fun availability(@RequestParam date: String): Mono<AvailabilityResponse> {
        val d = LocalDate.parse(date)
        return reservationService.availability(d)
    }

    @PostMapping
    fun create(@RequestBody req: CreateReservationRequest, auth: Authentication): Mono<Reservation> {
        return reservationService.create(req, auth.name)
    }

    @GetMapping("/my")
    fun my(auth: Authentication): Mono<List<Reservation>> {
        return reservationService.myReservations(auth.name)
    }
}