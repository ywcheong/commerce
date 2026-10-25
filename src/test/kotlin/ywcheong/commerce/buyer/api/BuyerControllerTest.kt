package ywcheong.commerce.buyer.api

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import ywcheong.commerce.TestSetup
import ywcheong.commerce.identity.api.IdentityControllerTestHelper
import ywcheong.commerce.seller.api.SellerControllerTestHelper
import java.util.UUID

@TestSetup
class BuyerControllerTest(
    mockMvc: MockMvc,
) {
    private val identityHelper = IdentityControllerTestHelper(mockMvc)
    private val sellerHelper = SellerControllerTestHelper(mockMvc)
    private val helper = BuyerControllerTestHelper(mockMvc)

    @Test
    fun `사용자는 구매자로 등록할 수 있다`() {
        // given
        val identityId = identityHelper.createIdentity()

        // when
        val result = helper.registerBuyer(identityId)

        // then
        result.status shouldBe HttpStatus.CREATED
        result.buyerId shouldNotBe null
    }

    @Test
    fun `이미 구매자로 등록한 사용자는 다시 등록할 수 없다`() {
        // given
        val identityId = identityHelper.createIdentity()
        helper.registerBuyer(identityId)

        // when
        val result = helper.registerBuyer(identityId)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "BUYER_ALREADY_REGISTERED"
    }

    @Test
    fun `존재하지 않는 사용자는 구매자로 등록할 수 없다`() {
        // when
        val result = helper.registerBuyer(UUID.randomUUID().toString())

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "IDENTITY_NOT_FOUND"
    }

    @Test
    fun `식별되지 않은 사용자는 구매자로 등록할 수 없다`() {
        // when
        val result = helper.registerBuyer(null)

        // then
        result.status shouldBe HttpStatus.UNAUTHORIZED
        result.code shouldBe "UNAUTHORIZED"
    }

    @Test
    fun `구매자는 본인 구매자 정보를 조회할 수 있다`() {
        // given
        val identityId = identityHelper.createIdentity()
        val buyerId = helper.registerBuyer(identityId).buyerId

        // when
        val result = helper.findMyBuyer(identityId)

        // then
        result.status shouldBe HttpStatus.OK
        result.buyerId shouldBe buyerId
    }

    @Test
    fun `구매자가 아닌 사용자는 구매자 전용 기능에 접근할 수 없다`() {
        // given
        val identityId = identityHelper.createIdentity()

        // when
        val result = helper.findMyBuyer(identityId)

        // then
        result.status shouldBe HttpStatus.FORBIDDEN
        result.code shouldBe "FORBIDDEN"
    }

    @Test
    fun `식별되지 않은 사용자는 구매자 전용 기능에 접근할 수 없다`() {
        // when
        val result = helper.findMyBuyer(null)

        // then
        result.status shouldBe HttpStatus.UNAUTHORIZED
        result.code shouldBe "UNAUTHORIZED"
    }

    @Test
    fun `판매자로만 등록한 사용자는 구매자 전용 기능에 접근할 수 없다`() {
        // given
        val identityId = identityHelper.createIdentity()
        sellerHelper.registerSeller(identityId)

        // when
        val result = helper.findMyBuyer(identityId)

        // then
        result.status shouldBe HttpStatus.FORBIDDEN
        result.code shouldBe "FORBIDDEN"
    }

    @Test
    fun `판매자로 등록한 사용자도 구매자로 등록할 수 있다`() {
        // given
        val identityId = identityHelper.createIdentity()
        sellerHelper.registerSeller(identityId)

        // when
        val result = helper.registerBuyer(identityId)

        // then
        result.status shouldBe HttpStatus.CREATED
        helper.findMyBuyer(identityId).status shouldBe HttpStatus.OK
        sellerHelper.findMySeller(identityId).status shouldBe HttpStatus.OK
    }
}
