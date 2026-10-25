package ywcheong.commerce.order.api

import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver.Companion.IDENTITY_HEADER
import ywcheong.commerce.errorCode

class OrderControllerTestHelper(
    mockMvc: MockMvc,
) {
    private val client = RestTestClient.bindTo(mockMvc).build()

    fun placeOrder(
        identityId: String,
        shopItemId: String,
        quantity: Int,
    ): OrderResult {
        val result =
            client
                .post()
                .uri("/orders")
                .header(IDENTITY_HEADER, identityId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapOf("shopItemId" to shopItemId, "quantity" to quantity))
                .exchange()
                .returnResult<String>()
        val orderId: String? = if (result.status.is2xxSuccessful) JsonPath.read(result.responseBody, "\$.orderId") else null
        return OrderResult(
            status = result.status,
            orderId = orderId,
            body = null,
            code = errorCode(result.responseBody),
        )
    }

    fun findOrder(
        identityId: String,
        orderId: String,
    ): OrderResult {
        val result =
            client
                .get()
                .uri("/orders/{orderId}", orderId)
                .header(IDENTITY_HEADER, identityId)
                .exchange()
                .returnResult<String>()
        val order: Map<String, Any?>? = if (result.status.is2xxSuccessful) JsonPath.read(result.responseBody, "\$") else null
        return OrderResult(
            status = result.status,
            orderId = order?.get("orderId") as String?,
            body = order,
            code = errorCode(result.responseBody),
        )
    }
}

data class OrderResult(
    val status: HttpStatusCode,
    val orderId: String?,
    val body: Map<String, Any?>?,
    val code: String?,
)
