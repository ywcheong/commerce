package ywcheong.commerce

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import ywcheong.commerce.identity.domain.IdentityRepository
import ywcheong.commerce.identity.infra.FakeIdentityRepository

@TestConfiguration
class TestSetupBeans {
    @Bean
    fun identityRepository(): IdentityRepository = FakeIdentityRepository()
}
