package ywcheong.commerce

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import ywcheong.commerce.user.domain.UserRepository
import ywcheong.commerce.user.infra.FakeUserRepository

@TestConfiguration
class TestSetupBeans {
    @Bean
    fun userRepository(): UserRepository = FakeUserRepository()
}
