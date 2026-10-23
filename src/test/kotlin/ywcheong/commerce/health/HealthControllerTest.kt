package ywcheong.commerce.health

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody
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
}
