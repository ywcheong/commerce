package ywcheong.commerce.buyer.infra

import ywcheong.commerce.buyer.domain.Buyer
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.buyer.domain.BuyerRepository
import ywcheong.commerce.identity.domain.IdentityId
import java.util.concurrent.ConcurrentHashMap

class FakeBuyerRepository : BuyerRepository {
    private val buyers = ConcurrentHashMap<BuyerId, Buyer>()

    override fun save(buyer: Buyer): Buyer {
        buyers[buyer.id] = buyer
        return buyer
    }

    override fun findByIdOrNull(id: BuyerId): Buyer? = buyers[id]

    override fun findByIdentityId(identityId: IdentityId): Buyer? = buyers.values.find { it.identityId == identityId }
}
