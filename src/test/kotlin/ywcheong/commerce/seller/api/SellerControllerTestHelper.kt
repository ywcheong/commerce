package ywcheong.commerce.seller.api

import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpStatusCode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver.Companion.IDENTITY_HEADER
import ywcheong.commerce.errorCode

class SellerControllerTestHelper(
    mockMvc: MockMvc,
) {
    private val client = RestTestClient.bindTo(mockMvc).build()

    fun registerSeller(identityId: String?): SellerResult {
        val result =
            client
                .post()
                .uri("/sellers")
                .headers { if (identityId != null) it.set(IDENTITY_HEADER, identityId) }
                .exchange()
                .returnResult<String>()
        return SellerResult(
            status = result.status,
            sellerId = sellerId(status = result.status, body = result.responseBody),
            code = errorCode(result.responseBody),
        )
    }

    fun findMySeller(identityId: String?): SellerResult {
        val result =
            client
                .get()
                .uri("/sellers/me")
                .headers { if (identityId != null) it.set(IDENTITY_HEADER, identityId) }
                .exchange()
                .returnResult<String>()
        return SellerResult(
            status = result.status,
            sellerId = sellerId(status = result.status, body = result.responseBody),
            code = errorCode(result.responseBody),
        )
    }

    private fun sellerId(
        status: HttpStatusCode,
        body: String?,
    ): String? = if (status.is2xxSuccessful) JsonPath.read(body, "\$.sellerId") else null
}

data class SellerResult(
    val status: HttpStatusCode,
    val sellerId: String?,
    val code: String?,
)
