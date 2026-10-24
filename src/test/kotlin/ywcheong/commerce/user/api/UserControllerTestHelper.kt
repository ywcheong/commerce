package ywcheong.commerce.user.api

import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpStatusCode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult

private const val USER_ID_HEADER = "X-User-Id"

class UserControllerTestHelper(
    mockMvc: MockMvc,
) {
    private val client = RestTestClient.bindTo(mockMvc).build()

    fun createUser(): String {
        val result =
            client
                .post()
                .uri("/api-public/users")
                .exchange()
                .returnResult<String>()
        check(result.status.value() == 201) { "사용자 생성 실패: ${result.status}" }
        return JsonPath.read(result.responseBody, "\$.userId")
    }

    fun findUser(
        userId: String,
        currentUserId: String? = userId,
    ): UserResult {
        val result =
            client
                .get()
                .uri("/api/users/{userId}", userId)
                .headers { if (currentUserId != null) it.set(USER_ID_HEADER, currentUserId) }
                .exchange()
                .returnResult<String>()
        val foundUserId: String? =
            if (result.status.is2xxSuccessful) JsonPath.read(result.responseBody, "\$.userId") else null
        return UserResult(result.status, foundUserId, errorCode(result.responseBody))
    }

    fun deleteUser(
        userId: String,
        currentUserId: String? = userId,
    ): UserResult {
        val result =
            client
                .delete()
                .uri("/api/users/{userId}", userId)
                .headers { if (currentUserId != null) it.set(USER_ID_HEADER, currentUserId) }
                .exchange()
                .returnResult<String>()
        return UserResult(result.status, null, errorCode(result.responseBody))
    }

    private fun errorCode(body: String?): String? =
        if (body.isNullOrBlank()) null else JsonPath.read<Map<String, Any?>>(body, "\$")["code"] as String?
}

data class UserResult(
    val status: HttpStatusCode,
    val userId: String?,
    val code: String?,
)
