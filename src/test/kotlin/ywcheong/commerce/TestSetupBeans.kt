package ywcheong.commerce

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import ywcheong.commerce.buyer.domain.BuyerRepository
import ywcheong.commerce.buyer.infra.FakeBuyerRepository
import ywcheong.commerce.identity.domain.IdentityRepository
import ywcheong.commerce.identity.infra.FakeIdentityRepository
import ywcheong.commerce.seller.domain.SellerRepository
import ywcheong.commerce.seller.infra.FakeSellerRepository

@TestConfiguration
class TestSetupBeans {
    @Bean
    fun identityRepository(): IdentityRepository = FakeIdentityRepository()

    @Bean
    fun buyerRepository(): BuyerRepository = FakeBuyerRepository()

    @Bean
    fun sellerRepository(): SellerRepository = FakeSellerRepository()
}
