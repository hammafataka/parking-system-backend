package dev.mfataka.parkingsystembackend.model

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 23:36
 */
data class BaseResponse<T>(val result: String,
                           val resultMessage: String?,
                           val data: T? = null) {

    companion object {
        @JvmStatic
        fun <T> ok(item: T) = BaseResponse(OK, OK, item)
        fun <T> failed(resultMessage: String?) = BaseResponse<T>(FAILED, resultMessage)
    }
}

const val OK = "OK"
const val FAILED = "FAILED"