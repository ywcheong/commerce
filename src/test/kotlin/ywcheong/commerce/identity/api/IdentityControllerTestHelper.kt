package ywcheong.commerce.identity.api

import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpStatusCode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver.Companion.IDENTITY_HEADER
import ywcheong.commerce.errorCode

class IdentityControllerTestHelper(
    mockMvc: MockMvc,
) {
    private val client = RestTestClient.bindTo(mockMvc).build()

    fun createIdentity(): String {
        val result =
            client
                .post()
                .uri("/identities")
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
                .uri("/identities/{identityId}", identityId)
                .headers { if (currentIdentityId != null) it.set(IDENTITY_HEADER, currentIdentityId) }
                .exchange()
                .returnResult<String>()
        val foundIdentityId: String? =
            if (result.status.is2xxSuccessful) JsonPath.read(result.responseBody, "\$.identityId") else null
        return IdentityResult(
            status = result.status,
            identityId = foundIdentityId,
            code = errorCode(result.responseBody),
        )
    }

    fun deleteIdentity(
        identityId: String,
        currentIdentityId: String? = identityId,
    ): IdentityResult {
        val result =
            client
                .delete()
                .uri("/identities/{identityId}", identityId)
                .headers { if (currentIdentityId != null) it.set(IDENTITY_HEADER, currentIdentityId) }
                .exchange()
                .returnResult<String>()
        return IdentityResult(
            status = result.status,
            identityId = null,
            code = errorCode(result.responseBody),
        )
    }
}

data class IdentityResult(
    val status: HttpStatusCode,
    val identityId: String?,
    val code: String?,
)
