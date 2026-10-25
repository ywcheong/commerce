package ywcheong.commerce.shopitem.api

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import ywcheong.commerce.TestSetup
import ywcheong.commerce.identity.api.IdentityControllerTestHelper
import ywcheong.commerce.seller.api.SellerControllerTestHelper
import java.util.UUID

@TestSetup
class ShopItemControllerTest(
    mockMvc: MockMvc,
) {
    private val identityHelper = IdentityControllerTestHelper(mockMvc)
    private val sellerHelper = SellerControllerTestHelper(mockMvc)
    private val helper = ShopItemControllerTestHelper(mockMvc)

    private fun createSeller(): Pair<String, String> {
        val identityId = identityHelper.createIdentity()
        return identityId to sellerHelper.registerSeller(identityId).sellerId!!
    }

    @Test
    fun `판매자가 등록한 상품은 누구나 조회할 수 있다`() {
        // given
        val (identityId, sellerId) = createSeller()

        // when
        val shopItemId = helper.registerShopItem(identityId, name = "키보드", price = 30_000, stock = 5).shopItemId!!

        // then
        val result = helper.findShopItem(shopItemId)
        result.status shouldBe HttpStatus.OK
        result.body shouldBe
            mapOf(
                "shopItemId" to shopItemId,
                "sellerId" to sellerId,
                "name" to "키보드",
                "price" to 30_000,
                "stock" to 5,
            )
    }

    @Test
    fun `등록한 상품은 상품 목록에 포함된다`() {
        // given
        val (identityId, _) = createSeller()

        // when
        val shopItemId = helper.registerShopItem(identityId).shopItemId!!

        // then
        helper.findShopItemIds() shouldContain shopItemId
    }

    @Test
    fun `판매자가 아닌 사용자는 상품을 등록할 수 없다`() {
        // given
        val identityId = identityHelper.createIdentity()

        // when
        val result = helper.registerShopItem(identityId)

        // then
        result.status shouldBe HttpStatus.FORBIDDEN
        result.code shouldBe "FORBIDDEN"
    }

    @Test
    fun `상품 이름이 비어 있으면 등록할 수 없다`() {
        // given
        val (identityId, _) = createSeller()

        // when
        val result = helper.registerShopItem(identityId, name = " ")

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "INVALID_SHOP_ITEM_NAME"
    }

    @Test
    fun `가격이 음수이면 등록할 수 없다`() {
        // given
        val (identityId, _) = createSeller()

        // when
        val result = helper.registerShopItem(identityId, price = -1)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "INVALID_MONEY"
    }

    @Test
    fun `재고가 음수이면 등록할 수 없다`() {
        // given
        val (identityId, _) = createSeller()

        // when
        val result = helper.registerShopItem(identityId, stock = -1)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "INVALID_QUANTITY"
    }

    @Test
    fun `존재하지 않는 상품은 조회할 수 없다`() {
        // when
        val result = helper.findShopItem(UUID.randomUUID().toString())

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "SHOP_ITEM_NOT_FOUND"
    }
}
