package ywcheong.commerce.identity.domain

interface IdentityRepository {
    fun save(identity: Identity): Identity

    fun findByIdOrNull(id: IdentityId): Identity?

    fun delete(identity: Identity)
}
