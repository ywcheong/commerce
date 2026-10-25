package ywcheong.commerce.buyer.domain

import ywcheong.commerce.identity.domain.IdentityId

interface BuyerRepository {
    fun save(buyer: Buyer): Buyer

    fun findByIdOrNull(id: BuyerId): Buyer?

    fun findByIdentityId(identityId: IdentityId): Buyer?
}
