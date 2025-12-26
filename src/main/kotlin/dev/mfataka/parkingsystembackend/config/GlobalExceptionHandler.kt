package dev.mfataka.parkingsystembackend.config

import dev.mfataka.parkingsystembackend.model.BaseResponse
import dev.mfataka.parkingsystembackend.model.FAILED
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 23:35
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKey(): ResponseEntity<BaseResponse<*>> =
        ResponseEntity.badRequest().body(
            BaseResponse<Any>(FAILED, "duplicate key")
        )
}