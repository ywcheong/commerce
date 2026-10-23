package ywcheong.commerce.user.infra

import ywcheong.commerce.user.domain.User
import ywcheong.commerce.user.domain.UserId
import ywcheong.commerce.user.domain.UserRepository
import java.util.concurrent.ConcurrentHashMap

class FakeUserRepository : UserRepository {
    private val users = ConcurrentHashMap<UserId, User>()

    override fun save(user: User): User {
        users[user.id] = user
        return user
    }

    override fun findByIdOrNull(id: UserId): User? = users[id]

    override fun delete(user: User) {
        users.remove(user.id)
    }
}
