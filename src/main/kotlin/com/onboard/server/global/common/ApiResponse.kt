package com.onboard.server.global.common

import org.springframework.http.HttpStatus

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T,
) {
    companion object {
        fun <T> of(data: T, status: HttpStatus): ApiResponse<T> =
            ApiResponse(
                status = status.value(),
                message = status.reasonPhrase,
                data = data,
            )

        fun <T> ok(data: T): ApiResponse<T> = of(data, HttpStatus.OK)

        fun <T> create(data: T): ApiResponse<T> = of(data, HttpStatus.CREATED)

        fun <T> noContent(data: T): ApiResponse<T> = of(data, HttpStatus.NO_CONTENT)
    }
}
