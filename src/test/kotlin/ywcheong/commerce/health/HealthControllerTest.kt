package ywcheong.commerce.health

import com.jayway.jsonpath.JsonPath
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody
import org.springframework.test.web.servlet.client.returnResult
import ywcheong.commerce.TestSetup

@TestSetup
class HealthControllerTest(
    mockMvc: MockMvc,
) {
    private val client = RestTestClient.bindTo(mockMvc).build()

    @Test
    fun `헬스체크는 ok를 반환한다`() {
        client
            .get()
            .uri("/health/ok")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<String>()
            .isEqualTo("ok")
    }

    @Test
    fun `비즈니스 오류 예시는 422로 응답한다`() {
        // when
        val result =
            client
                .get()
                .uri("/health/error/business")
                .exchange()
                .returnResult<String>()

        // then
        result.status.value() shouldBe 422
        JsonPath.read<String>(result.responseBody, "\$.code") shouldBe "EXAMPLE_BUSINESS_ERROR"
    }

    @Test
    fun `서버 오류 예시는 500으로 응답한다`() {
        // when
        val result =
            client
                .get()
                .uri("/health/error/server")
                .exchange()
                .returnResult<String>()

        // then
        result.status.value() shouldBe 500
        JsonPath.read<String>(result.responseBody, "\$.code") shouldBe "INTERNAL_SERVER_ERROR"
    }
}
