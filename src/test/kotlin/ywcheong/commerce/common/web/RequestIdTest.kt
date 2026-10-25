package ywcheong.commerce.common.web

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import ywcheong.commerce.TestSetup
import java.util.UUID

@TestSetup
class RequestIdTest(
    mockMvc: MockMvc,
) {
    private val client = RestTestClient.bindTo(mockMvc).build()

    private fun requestId(uri: String): Pair<HttpStatus, String?> {
        val result =
            client
                .get()
                .uri(uri)
                .exchange()
                .returnResult<String>()
        return HttpStatus.valueOf(result.status.value()) to result.responseHeaders.getFirst("X-Request-Id")
    }

    @Test
    fun `성공 응답은 UUIDv7 요청 Id를 헤더로 반환한다`() {
        // when
        val (status, requestId) = requestId("/health/ok")

        // then
        status shouldBe HttpStatus.OK
        UUID.fromString(requestId).version() shouldBe 7
    }

    @Test
    fun `인증 실패 응답도 요청 Id를 헤더로 반환한다`() {
        // when
        val (status, requestId) = requestId("/identities/${UUID.randomUUID()}")

        // then
        status shouldBe HttpStatus.UNAUTHORIZED
        requestId shouldNotBe null
    }

    @Test
    fun `요청 Id는 요청마다 다르다`() {
        // when
        val (_, first) = requestId("/health/ok")
        val (_, second) = requestId("/health/ok")

        // then
        first shouldNotBe second
    }
}
