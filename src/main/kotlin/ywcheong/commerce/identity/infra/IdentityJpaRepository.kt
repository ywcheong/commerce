package ywcheong.commerce.identity.infra

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ywcheong.commerce.identity.domain.Identity
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.identity.domain.IdentityRepository

@Repository
interface IdentityJpaRepository :
    JpaRepository<Identity, IdentityId>,
    IdentityRepository {
    override fun findByIdOrNull(id: IdentityId): Identity? = findById(id).orElse(null)
}
