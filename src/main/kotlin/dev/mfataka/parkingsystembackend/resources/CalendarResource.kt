package dev.mfataka.parkingsystembackend.resources

import dev.mfataka.parkingsystembackend.model.BaseResponse
import dev.mfataka.parkingsystembackend.model.holiday.Holiday
import dev.mfataka.parkingsystembackend.service.CalendarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 19.12.2025 0:01
 */
@RestController
@RequestMapping(path =  ["/api/v1/calendar"])
class CalendarResource(@Autowired private val calendarService: CalendarService) {

    @GetMapping("/holidays")
    fun findHolidays(): BaseResponse<List<Holiday>> {
        return calendarService.findHolidays()
            ?.let { BaseResponse.ok(it) }
            ?: BaseResponse.failed("findHolidays failed")
    }
}