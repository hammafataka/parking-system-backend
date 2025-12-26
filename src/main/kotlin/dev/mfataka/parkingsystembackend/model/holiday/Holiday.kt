package dev.mfataka.parkingsystembackend.model.holiday

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 23:52
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Holiday(
     val name: List<HolidayName>,
    @JsonFormat(pattern = "yyyy-MM-dd")
     val startDate: LocalDate,
    @JsonFormat(pattern = "yyyy-MM-dd")
     val endDate: LocalDate)


@JsonIgnoreProperties(ignoreUnknown = true)
data class HolidayName( val language: String?,  val text: String?)