package ywcheong.commerce.user.api

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import ywcheong.commerce.TestSetup
import java.util.UUID

@TestSetup
class UserControllerTest(
    mockMvc: MockMvc,
) {
    private val helper = UserControllerTestHelper(mockMvc)

    @Test
    fun `생성한 사용자는 본인이 조회할 수 있다`() {
        // given
        val userId = helper.createUser()

        // when
        val result = helper.findUser(userId)

        // then
        result.status shouldBe HttpStatus.OK
        result.userId shouldBe userId
    }

    @Test
    fun `다른 사용자는 조회할 수 없다`() {
        // given
        val userId = helper.createUser()
        val otherUserId = helper.createUser()

        // when
        val result = helper.findUser(userId, currentUserId = otherUserId)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "USER_FORBIDDEN"
    }

    @Test
    fun `존재하지 않는 사용자는 조회할 수 없다`() {
        // given
        val userId = UUID.randomUUID().toString()

        // when
        val result = helper.findUser(userId)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "USER_NOT_FOUND"
    }

    @Test
    fun `사용자 식별 헤더가 없으면 인증되지 않는다`() {
        // given
        val userId = helper.createUser()

        // when
        val result = helper.findUser(userId, currentUserId = null)

        // then
        result.status shouldBe HttpStatus.UNAUTHORIZED
    }

    @Test
    fun `사용자 식별 헤더가 UUID 형식이 아니면 인증되지 않는다`() {
        // given
        val userId = helper.createUser()

        // when
        val result = helper.findUser(userId, currentUserId = "not-uuid")

        // then
        result.status shouldBe HttpStatus.UNAUTHORIZED
    }

    @Test
    fun `경로의 사용자 Id가 UUID 형식이 아니면 잘못된 요청이다`() {
        // given
        val userId = helper.createUser()

        // when
        val result = helper.findUser("not-uuid", currentUserId = userId)

        // then
        result.status shouldBe HttpStatus.BAD_REQUEST
        result.code shouldBe "BAD_REQUEST"
    }

    @Test
    fun `본인을 삭제하면 더 이상 조회할 수 없다`() {
        // given
        val userId = helper.createUser()

        // when
        val result = helper.deleteUser(userId)

        // then
        result.status shouldBe HttpStatus.NO_CONTENT
        helper.findUser(userId).code shouldBe "USER_NOT_FOUND"
    }

    @Test
    fun `다른 사용자는 삭제할 수 없다`() {
        // given
        val userId = helper.createUser()
        val otherUserId = helper.createUser()

        // when
        val result = helper.deleteUser(userId, currentUserId = otherUserId)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "USER_FORBIDDEN"
        helper.findUser(userId).status shouldBe HttpStatus.OK
    }
}
