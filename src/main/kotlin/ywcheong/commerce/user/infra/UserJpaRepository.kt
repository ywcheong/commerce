package ywcheong.commerce.user.infra

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ywcheong.commerce.user.domain.User
import ywcheong.commerce.user.domain.UserId
import ywcheong.commerce.user.domain.UserRepository

@Repository
interface UserJpaRepository :
    JpaRepository<User, UserId>,
    UserRepository {
    override fun findByIdOrNull(id: UserId): User? = findById(id).orElse(null)
}
