package dev.mfataka.parkingsystembackend.service

import dev.mfataka.parkingsystembackend.model.holiday.Holiday
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import reactor.core.publisher.Mono
import java.time.LocalDate

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 23:51
 */
@Service
class CalendarService {
    private val log = KotlinLogging.logger {}
    private val holidayCache = mutableListOf<Holiday>()

    fun findHolidays(): Mono<List<Holiday>> {
        if (!holidayCache.isEmpty()) {
            return Mono.just(holidayCache)
        }
        val today = LocalDate.now()
        val startDate = today.toString()
        val endDate = today.plusYears(1).toString()
        return WebClient.create()
            .get()
            .uri("https://openholidaysapi.org/PublicHolidays?countryIsoCode=CZ&languageIsoCode=CZ&validFrom=$startDate&validTo=$endDate")
            .exchangeToMono { response -> response.toEntity<List<Holiday>>() }
            .filter { it.statusCode.is2xxSuccessful }
            .map { response -> response.body as List<Holiday> }
            .doOnNext { holidays -> holidayCache.addAll(holidays) }
            .doOnNext { log.info { "successfully retrieved ${it.size} holidays" } }
    }

    fun isHoliday(date: LocalDate): Boolean {
        return holidayCache.any { h ->
            !date.isBefore(h.startDate) && !date.isAfter(h.endDate)
        }
    }
}