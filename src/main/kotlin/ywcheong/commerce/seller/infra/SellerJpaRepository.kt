package ywcheong.commerce.seller.infra

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ywcheong.commerce.seller.domain.Seller
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.seller.domain.SellerRepository

@Repository
interface SellerJpaRepository :
    JpaRepository<Seller, SellerId>,
    SellerRepository {
    override fun findByIdOrNull(id: SellerId): Seller? = findById(id).orElse(null)
}
