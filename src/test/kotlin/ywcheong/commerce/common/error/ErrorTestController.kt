package ywcheong.commerce.common.error

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ErrorTestController {
    @GetMapping("/api-public/test/unhandled-error")
    fun unhandledError(): Nothing = error(ErrorResponseTest.INTERNAL_MESSAGE)
}
