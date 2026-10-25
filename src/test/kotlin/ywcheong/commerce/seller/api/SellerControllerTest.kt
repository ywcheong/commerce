package ywcheong.commerce.seller.api

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import ywcheong.commerce.TestSetup
import ywcheong.commerce.identity.api.IdentityControllerTestHelper
import java.util.UUID

@TestSetup
class SellerControllerTest(
    mockMvc: MockMvc,
) {
    private val identityHelper = IdentityControllerTestHelper(mockMvc)
    private val helper = SellerControllerTestHelper(mockMvc)

    @Test
    fun `사용자는 판매자로 등록할 수 있다`() {
        // given
        val identityId = identityHelper.createIdentity()

        // when
        val result = helper.registerSeller(identityId)

        // then
        result.status shouldBe HttpStatus.CREATED
        result.sellerId shouldNotBe null
    }

    @Test
    fun `이미 판매자로 등록한 사용자는 다시 등록할 수 없다`() {
        // given
        val identityId = identityHelper.createIdentity()
        helper.registerSeller(identityId)

        // when
        val result = helper.registerSeller(identityId)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "SELLER_ALREADY_REGISTERED"
    }

    @Test
    fun `존재하지 않는 사용자는 판매자로 등록할 수 없다`() {
        // when
        val result = helper.registerSeller(UUID.randomUUID().toString())

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "IDENTITY_NOT_FOUND"
    }

    @Test
    fun `식별되지 않은 사용자는 판매자로 등록할 수 없다`() {
        // when
        val result = helper.registerSeller(null)

        // then
        result.status shouldBe HttpStatus.UNAUTHORIZED
        result.code shouldBe "UNAUTHORIZED"
    }

    @Test
    fun `판매자는 본인 판매자 정보를 조회할 수 있다`() {
        // given
        val identityId = identityHelper.createIdentity()
        val sellerId = helper.registerSeller(identityId).sellerId

        // when
        val result = helper.findMySeller(identityId)

        // then
        result.status shouldBe HttpStatus.OK
        result.sellerId shouldBe sellerId
    }

    @Test
    fun `판매자가 아닌 사용자는 판매자 전용 기능에 접근할 수 없다`() {
        // given
        val identityId = identityHelper.createIdentity()

        // when
        val result = helper.findMySeller(identityId)

        // then
        result.status shouldBe HttpStatus.FORBIDDEN
        result.code shouldBe "FORBIDDEN"
    }

    @Test
    fun `식별되지 않은 사용자는 판매자 전용 기능에 접근할 수 없다`() {
        // when
        val result = helper.findMySeller(null)

        // then
        result.status shouldBe HttpStatus.UNAUTHORIZED
        result.code shouldBe "UNAUTHORIZED"
    }
}
