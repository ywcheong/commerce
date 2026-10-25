package ywcheong.commerce.seller.infra

import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.seller.domain.Seller
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.seller.domain.SellerRepository
import java.util.concurrent.ConcurrentHashMap

class FakeSellerRepository : SellerRepository {
    private val sellers = ConcurrentHashMap<SellerId, Seller>()

    override fun save(seller: Seller): Seller {
        sellers[seller.id] = seller
        return seller
    }

    override fun findByIdOrNull(id: SellerId): Seller? = sellers[id]

    override fun findByIdentityId(identityId: IdentityId): Seller? = sellers.values.find { it.identityId == identityId }
}
