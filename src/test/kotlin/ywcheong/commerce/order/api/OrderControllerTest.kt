package ywcheong.commerce.order.api

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import ywcheong.commerce.TestSetup
import ywcheong.commerce.buyer.api.BuyerControllerTestHelper
import ywcheong.commerce.identity.api.IdentityControllerTestHelper
import ywcheong.commerce.seller.api.SellerControllerTestHelper
import ywcheong.commerce.shop.api.ShopItemControllerTestHelper
import java.util.UUID

@TestSetup
class OrderControllerTest(
    mockMvc: MockMvc,
) {
    private val identityHelper = IdentityControllerTestHelper(mockMvc)
    private val sellerHelper = SellerControllerTestHelper(mockMvc)
    private val buyerHelper = BuyerControllerTestHelper(mockMvc)
    private val shopItemHelper = ShopItemControllerTestHelper(mockMvc)
    private val helper = OrderControllerTestHelper(mockMvc)

    private fun createShopItem(
        price: Long = 10_000,
        stock: Int = 10,
    ): String {
        val identityId = identityHelper.createIdentity()
        sellerHelper.registerSeller(identityId)
        return shopItemHelper.registerShopItem(identityId = identityId, price = price, stock = stock).shopItemId!!
    }

    private fun createBuyer(): String {
        val identityId = identityHelper.createIdentity()
        buyerHelper.registerBuyer(identityId)
        return identityId
    }

    @Test
    fun `구매자는 상품을 주문하고 본인 주문을 조회할 수 있다`() {
        // given
        val shopItemId = createShopItem(price = 3_000)
        val buyer = createBuyer()

        // when
        val orderId = helper.placeOrder(identityId = buyer, shopItemId = shopItemId, quantity = 2).orderId!!

        // then
        val result = helper.findOrder(identityId = buyer, orderId = orderId)
        result.status shouldBe HttpStatus.OK
        result.body shouldBe
            mapOf(
                "orderId" to orderId,
                "shopItemId" to shopItemId,
                "quantity" to 2,
                "totalPrice" to 6_000,
            )
    }

    @Test
    fun `주문하면 상품 재고가 주문 수량만큼 줄어든다`() {
        // given
        val shopItemId = createShopItem(stock = 10)
        val buyer = createBuyer()

        // when
        helper.placeOrder(identityId = buyer, shopItemId = shopItemId, quantity = 3)

        // then
        shopItemHelper.findShopItem(shopItemId).body!!["stock"] shouldBe 7
    }

    @Test
    fun `재고보다 많이 주문할 수 없다`() {
        // given
        val shopItemId = createShopItem(stock = 1)
        val buyer = createBuyer()

        // when
        val result = helper.placeOrder(identityId = buyer, shopItemId = shopItemId, quantity = 2)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "OUT_OF_STOCK"
        shopItemHelper.findShopItem(shopItemId).body!!["stock"] shouldBe 1
    }

    @Test
    fun `주문 수량이 0이면 주문할 수 없다`() {
        // given
        val shopItemId = createShopItem()
        val buyer = createBuyer()

        // when
        val result = helper.placeOrder(identityId = buyer, shopItemId = shopItemId, quantity = 0)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "INVALID_ORDER_QUANTITY"
    }

    @Test
    fun `존재하지 않는 상품은 주문할 수 없다`() {
        // given
        val buyer = createBuyer()

        // when
        val result = helper.placeOrder(identityId = buyer, shopItemId = UUID.randomUUID().toString(), quantity = 1)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "SHOP_ITEM_NOT_FOUND"
    }

    @Test
    fun `구매자가 아닌 사용자는 주문할 수 없다`() {
        // given
        val shopItemId = createShopItem()
        val identityId = identityHelper.createIdentity()

        // when
        val result = helper.placeOrder(identityId = identityId, shopItemId = shopItemId, quantity = 1)

        // then
        result.status shouldBe HttpStatus.FORBIDDEN
        result.code shouldBe "FORBIDDEN"
    }

    @Test
    fun `다른 구매자의 주문은 조회할 수 없다`() {
        // given
        val shopItemId = createShopItem()
        val orderId = helper.placeOrder(identityId = createBuyer(), shopItemId = shopItemId, quantity = 1).orderId!!
        val otherBuyer = createBuyer()

        // when
        val result = helper.findOrder(identityId = otherBuyer, orderId = orderId)

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "ORDER_FORBIDDEN"
    }

    @Test
    fun `존재하지 않는 주문은 조회할 수 없다`() {
        // given
        val buyer = createBuyer()

        // when
        val result = helper.findOrder(identityId = buyer, orderId = UUID.randomUUID().toString())

        // then
        result.status shouldBe HttpStatus.UNPROCESSABLE_CONTENT
        result.code shouldBe "ORDER_NOT_FOUND"
    }
}
