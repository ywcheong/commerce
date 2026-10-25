package ywcheong.commerce

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import ywcheong.commerce.buyer.domain.BuyerRepository
import ywcheong.commerce.buyer.infra.FakeBuyerRepository
import ywcheong.commerce.identity.domain.IdentityRepository
import ywcheong.commerce.identity.infra.FakeIdentityRepository
import ywcheong.commerce.order.domain.OrderRepository
import ywcheong.commerce.order.infra.FakeOrderRepository
import ywcheong.commerce.seller.domain.SellerRepository
import ywcheong.commerce.seller.infra.FakeSellerRepository
import ywcheong.commerce.shopitem.domain.ShopItemRepository
import ywcheong.commerce.shopitem.infra.FakeShopItemRepository

@TestConfiguration
class TestSetupBeans {
    @Bean
    fun identityRepository(): IdentityRepository = FakeIdentityRepository()

    @Bean
    fun buyerRepository(): BuyerRepository = FakeBuyerRepository()

    @Bean
    fun sellerRepository(): SellerRepository = FakeSellerRepository()

    @Bean
    fun shopItemRepository(): ShopItemRepository = FakeShopItemRepository()

    @Bean
    fun orderRepository(): OrderRepository = FakeOrderRepository()
}
