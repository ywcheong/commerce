package ywcheong.commerce.buyer.api

import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpStatusCode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver.Companion.IDENTITY_HEADER
import ywcheong.commerce.errorCode

class BuyerControllerTestHelper(
    mockMvc: MockMvc,
) {
    private val client = RestTestClient.bindTo(mockMvc).build()

    fun registerBuyer(identityId: String?): BuyerResult {
        val result =
            client
                .post()
                .uri("/buyers")
                .headers { if (identityId != null) it.set(IDENTITY_HEADER, identityId) }
                .exchange()
                .returnResult<String>()
        return BuyerResult(
            status = result.status,
            buyerId = buyerId(status = result.status, body = result.responseBody),
            code = errorCode(result.responseBody),
        )
    }

    fun findMyBuyer(identityId: String?): BuyerResult {
        val result =
            client
                .get()
                .uri("/buyers/me")
                .headers { if (identityId != null) it.set(IDENTITY_HEADER, identityId) }
                .exchange()
                .returnResult<String>()
        return BuyerResult(
            status = result.status,
            buyerId = buyerId(status = result.status, body = result.responseBody),
            code = errorCode(result.responseBody),
        )
    }

    private fun buyerId(
        status: HttpStatusCode,
        body: String?,
    ): String? = if (status.is2xxSuccessful) JsonPath.read(body, "\$.buyerId") else null
}

data class BuyerResult(
    val status: HttpStatusCode,
    val buyerId: String?,
    val code: String?,
)
