package dev.mfataka.parkingsystembackend.service


import dev.mfataka.parkingsystembackend.collection.Reservation
import dev.mfataka.parkingsystembackend.model.slot.ReserveSlotRequest
import dev.mfataka.parkingsystembackend.repository.ReservationRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDate

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 27.12.2025 18:06
 */

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val slotService: SlotService,
    private val calendarService: CalendarService
) {
    private val log = KotlinLogging.logger {}

    fun create(req: ReserveSlotRequest, userName: String): Mono<Reservation> {
        validateDate(req.reservedFrom.toLocalDate())
        validateDate(req.reservedUntil.toLocalDate())
        return reservationRepository.existsBySlotNameAndStartDttmAndEndDttm(req.slotName, req.reservedFrom, req.reservedUntil)
            .map {
                if (it) {
                    throw IllegalStateException("Slot ${req.slotName} is already reserved for ${req.reservedFrom} ${req.reservedUntil}")
                }
            }.flatMap {
                slotService.reserve(req, userName)
                    .switchIfEmpty(Mono.error { error("slot ${req.slotName} not found") })
                    .flatMap { slot ->
                        val reservation = Reservation(
                            slotName = slot.name,
                            garageName = slot.garageName,
                            userName = userName,
                            startDttm = req.reservedFrom,
                            endDttm = req.reservedUntil,
                        )
                        reservationRepository.save(reservation)
                    }
            }
            .doOnError { log.error { it } }

    }

    fun myReservations(userName: String): Mono<List<Reservation>> {
        return reservationRepository.findAllByUserNameOrderByStartDttmDesc(userName)
            .collectList()
    }

    private fun validateDate(date: LocalDate) {
        val today = LocalDate.now()
        val max = today.plusDays(2)

        if (date.isBefore(today) || date.isAfter(max)) {
            throw IllegalArgumentException("Date must be between $today and $max")
        }
        if (calendarService.isHoliday(date)) {
            throw IllegalArgumentException("Date $date is a holiday and cannot be reserved")
        }
    }
}