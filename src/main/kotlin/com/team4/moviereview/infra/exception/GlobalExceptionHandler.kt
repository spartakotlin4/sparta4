package com.team4.moviereview.infra.exception

import com.team4.moviereview.infra.exception.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun runtimeExceptionHandler(e : RuntimeException) : ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(e.message!!))
    }

    @ExceptionHandler(UnAuthorizeException::class)
    fun unAuthorizeExceptionHandler(e : UnAuthorizeException) : ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(e.message!!))
    }
}