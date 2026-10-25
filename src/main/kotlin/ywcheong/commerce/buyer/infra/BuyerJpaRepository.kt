package ywcheong.commerce.buyer.infra

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ywcheong.commerce.buyer.domain.Buyer
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.buyer.domain.BuyerRepository

@Repository
interface BuyerJpaRepository :
    JpaRepository<Buyer, BuyerId>,
    BuyerRepository {
    override fun findByIdOrNull(id: BuyerId): Buyer? = findById(id).orElse(null)
}
