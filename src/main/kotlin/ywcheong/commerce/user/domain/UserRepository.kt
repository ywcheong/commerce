package ywcheong.commerce.user.domain

interface UserRepository {
    fun save(user: User): User

    fun findByIdOrNull(id: UserId): User?

    fun delete(user: User)
}
