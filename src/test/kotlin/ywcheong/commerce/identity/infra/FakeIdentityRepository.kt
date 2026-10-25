package ywcheong.commerce.identity.infra

import ywcheong.commerce.identity.domain.Identity
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.identity.domain.IdentityRepository
import java.util.concurrent.ConcurrentHashMap

class FakeIdentityRepository : IdentityRepository {
    private val identities = ConcurrentHashMap<IdentityId, Identity>()

    override fun save(identity: Identity): Identity {
        identities[identity.id] = identity
        return identity
    }

    override fun findByIdOrNull(id: IdentityId): Identity? = identities[id]

    override fun delete(identity: Identity) {
        identities.remove(identity.id)
    }
}
