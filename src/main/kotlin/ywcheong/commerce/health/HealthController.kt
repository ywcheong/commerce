package ywcheong.commerce.health

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("/health/ok")
    fun health(): String = "ok"

    @GetMapping("/health/error/business")
    fun businessError(): Nothing = throw ExampleBusinessException()

    @Suppress("TooGenericExceptionThrown")
    @GetMapping("/health/error/server")
    fun serverError(): Nothing = throw RuntimeException("서버 오류 예시")
}
