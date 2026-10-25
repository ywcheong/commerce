package ywcheong.commerce.identity.api

import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpStatusCode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult

private const val IDENTITY_HEADER = "X-Identity"

class IdentityControllerTestHelper(
    mockMvc: MockMvc,
) {
    private val client = RestTestClient.bindTo(mockMvc).build()

    fun createIdentity(): String {
        val result =
            client
                .post()
                .uri("/api-public/identities")
                .exchange()
                .returnResult<String>()
        check(result.status.value() == 201) { "사용자 생성 실패: ${result.status}" }
        return JsonPath.read(result.responseBody, "\$.identityId")
    }

    fun findIdentity(
        identityId: String,
        currentIdentityId: String? = identityId,
    ): IdentityResult {
        val result =
            client
                .get()
                .uri("/api/identities/{identityId}", identityId)
                .headers { if (currentIdentityId != null) it.set(IDENTITY_HEADER, currentIdentityId) }
                .exchange()
                .returnResult<String>()
        val foundIdentityId: String? =
            if (result.status.is2xxSuccessful) JsonPath.read(result.responseBody, "\$.identityId") else null
        return IdentityResult(result.status, foundIdentityId, errorCode(result.responseBody))
    }

    fun deleteIdentity(
        identityId: String,
        currentIdentityId: String? = identityId,
    ): IdentityResult {
        val result =
            client
                .delete()
                .uri("/api/identities/{identityId}", identityId)
                .headers { if (currentIdentityId != null) it.set(IDENTITY_HEADER, currentIdentityId) }
                .exchange()
                .returnResult<String>()
        return IdentityResult(result.status, null, errorCode(result.responseBody))
    }

    private fun errorCode(body: String?): String? =
        if (body.isNullOrBlank()) null else JsonPath.read<Map<String, Any?>>(body, "\$")["code"] as String?
}

data class IdentityResult(
    val status: HttpStatusCode,
    val identityId: String?,
    val code: String?,
)
