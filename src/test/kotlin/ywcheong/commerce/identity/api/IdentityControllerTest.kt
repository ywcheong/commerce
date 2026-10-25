package ywcheong.commerce.identity.api

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import ywcheong.commerce.TestSetup
import java.util.UUID

@TestSetup
class IdentityControllerTest(
    mockMvc: MockMvc,
) {
    private val helper = IdentityControllerTestHelper(mockMvc)

    @Test
    fun `생성한 사용자는 본인이 조회할 수 있다`() {
        // given
        val identityId = helper.createIdentity()

        // when
        val result = helper.findIdentity(identityId)

        // then
        result.status shouldBe HttpStatus.OK
        result.identityId shouldBe identityId
    }

    @Test
    fun `다른 사용자는 조회할 수 없다`() {
        // given
        val identityId = helper.createIdentity()
        val otherIdentityId = helper.createIdentity()

        // when
        val result = helper.findIdentity(identityId, currentIdentityId = otherIdentityId)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "IDENTITY_FORBIDDEN"
    }

    @Test
    fun `존재하지 않는 사용자는 조회할 수 없다`() {
        // given
        val identityId = UUID.randomUUID().toString()

        // when
        val result = helper.findIdentity(identityId)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "IDENTITY_NOT_FOUND"
    }

    @Test
    fun `사용자 식별 헤더가 없으면 인증되지 않는다`() {
        // given
        val identityId = helper.createIdentity()

        // when
        val result = helper.findIdentity(identityId, currentIdentityId = null)

        // then
        result.status shouldBe HttpStatus.UNAUTHORIZED
        result.code shouldBe "UNAUTHORIZED"
    }

    @Test
    fun `사용자 식별 헤더가 UUID 형식이 아니면 인증되지 않는다`() {
        // given
        val identityId = helper.createIdentity()

        // when
        val result = helper.findIdentity(identityId, currentIdentityId = "not-uuid")

        // then
        result.status shouldBe HttpStatus.UNAUTHORIZED
        result.code shouldBe "UNAUTHORIZED"
    }

    @Test
    fun `경로의 사용자 Id가 UUID 형식이 아니면 잘못된 요청이다`() {
        // given
        val identityId = helper.createIdentity()

        // when
        val result = helper.findIdentity("not-uuid", currentIdentityId = identityId)

        // then
        result.status shouldBe HttpStatus.BAD_REQUEST
        result.code shouldBe "BAD_REQUEST"
    }

    @Test
    fun `본인을 삭제하면 더 이상 조회할 수 없다`() {
        // given
        val identityId = helper.createIdentity()

        // when
        val result = helper.deleteIdentity(identityId)

        // then
        result.status shouldBe HttpStatus.NO_CONTENT
        helper.findIdentity(identityId).code shouldBe "IDENTITY_NOT_FOUND"
    }

    @Test
    fun `다른 사용자는 삭제할 수 없다`() {
        // given
        val identityId = helper.createIdentity()
        val otherIdentityId = helper.createIdentity()

        // when
        val result = helper.deleteIdentity(identityId, currentIdentityId = otherIdentityId)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "IDENTITY_FORBIDDEN"
        helper.findIdentity(identityId).status shouldBe HttpStatus.OK
    }
}
