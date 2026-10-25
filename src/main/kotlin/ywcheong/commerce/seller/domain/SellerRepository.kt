package ywcheong.commerce.seller.domain

import ywcheong.commerce.identity.domain.IdentityId

interface SellerRepository {
    fun save(seller: Seller): Seller

    fun findByIdOrNull(id: SellerId): Seller?

    fun findByIdentityId(identityId: IdentityId): Seller?
}
