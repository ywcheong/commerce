package ywcheong.commerce.common.error

import com.jayway.jsonpath.JsonPath
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import ywcheong.commerce.TestSetup

@TestSetup
class ErrorResponseTest(
    mockMvc: MockMvc,
) {
    private val client = RestTestClient.bindTo(mockMvc).build()

    @Test
    fun `처리되지 않은 예외는 내부 메시지를 숨기고 500 ProblemDetail로 응답한다`() {
        // when
        val result =
            client
                .get()
                .uri("/api-public/test/unhandled-error")
                .exchange()
                .returnResult<String>()

        // then
        val body = result.responseBody.orEmpty()
        result.status.value() shouldBe 500
        result.responseHeaders.contentType shouldBe MediaType.APPLICATION_PROBLEM_JSON
        JsonPath.read<Map<String, Any?>>(body, "\$") shouldBe
            mapOf(
                "title" to "Internal Server Error",
                "status" to 500,
                "detail" to "서버 오류가 발생했습니다.",
                "instance" to "/api-public/test/unhandled-error",
                "code" to "INTERNAL_SERVER_ERROR",
            )
        body shouldNotContain INTERNAL_MESSAGE
    }

    @Test
    fun `존재하지 않는 경로는 404 ProblemDetail로 응답한다`() {
        // when
        val result =
            client
                .get()
                .uri("/api-public/no-such-path")
                .exchange()
                .returnResult<String>()

        // then
        result.status.value() shouldBe 404
        JsonPath.read<String>(result.responseBody, "\$.code") shouldBe "NOT_FOUND"
    }

    @Test
    fun `지원하지 않는 메서드는 405 ProblemDetail로 응답한다`() {
        // when
        val result =
            client
                .method(HttpMethod.POST)
                .uri("/health/ok")
                .exchange()
                .returnResult<String>()

        // then
        result.status.value() shouldBe 405
        JsonPath.read<String>(result.responseBody, "\$.code") shouldBe "METHOD_NOT_ALLOWED"
    }

    companion object {
        const val INTERNAL_MESSAGE = "내부 구현 정보"
    }
}
