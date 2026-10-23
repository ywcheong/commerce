package ywcheong.commerce.common.error

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {
    @ExceptionHandler
    fun handle(e: NotFoundException): ProblemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message)

    @ExceptionHandler
    fun handle(e: ForbiddenException): ProblemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.message)
}
