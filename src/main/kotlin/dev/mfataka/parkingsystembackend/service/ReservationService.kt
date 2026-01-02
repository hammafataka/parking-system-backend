package dev.mfataka.parkingsystembackend.service


import dev.mfataka.parkingsystembackend.collection.Reservation
import dev.mfataka.parkingsystembackend.model.reservation.AvailabilityResponse
import dev.mfataka.parkingsystembackend.model.reservation.CreateReservationRequest
import dev.mfataka.parkingsystembackend.repository.ReservationRepository
import dev.mfataka.parkingsystembackend.repository.SlotRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 27.12.2025 18:06
 */

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val slotRepository: SlotRepository,
    private val calendarService: CalendarService
) {
    private val log = KotlinLogging.logger {}
    private val fmt = DateTimeFormatter.ISO_DATE

    fun availability(date: LocalDate): Mono<AvailabilityResponse> {
        val dateStr = date.format(fmt)

        return reservationRepository.findAllByDate(date)
            .map { it.slotName }
            .collect(Collectors.toSet())
            .flatMap { reservedNames ->
                slotRepository.findAll()
                    .collectList()
                    .map { slots ->
                        val (available, reserved) = slots.partition { it.name !in reservedNames }

                        val availableDtos = available.map { it.toDto() }
                        val reservedDtos = reserved.map { it.toDto() }

                        AvailabilityResponse(dateStr, availableDtos, reservedDtos)
                    }
            }
            .doOnNext { r ->
                log.info { "availability date=${r.date} available=${r.availableSlots.size} reserved=${r.reservedSlots.size}" }
            }
    }

    fun create(req: CreateReservationRequest, userName: String): Mono<Reservation> {
        validateDate(req.date)

        return slotRepository.findByName(req.slotName)
            .switchIfEmpty(Mono.error { error("slot ${req.slotName} not found") })
            .flatMap { slot ->
                // unique index will also protect this, but we give a clear message
                reservationRepository.existsBySlotNameAndDate(req.slotName, req.date).map {
                    if (it) {
                        throw IllegalStateException("Slot ${req.slotName} is already reserved for ${req.date}")
                    }

                }.flatMap {
                    val reservation = Reservation(
                        slotName = slot.name,
                        garageName = slot.garageName,
                        userName = userName,
                        date = req.date
                    )
                    reservationRepository.save(reservation)
                }
            }


    }

    fun myReservations(userName: String): Mono<List<Reservation>> {
        return reservationRepository.findAllByUserNameOrderByDateDesc(userName)
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