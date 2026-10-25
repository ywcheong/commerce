package ywcheong.commerce.shop.infra

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ywcheong.commerce.shop.domain.ShopItem
import ywcheong.commerce.shop.domain.ShopItemId
import ywcheong.commerce.shop.domain.ShopItemRepository

@Repository
interface ShopItemJpaRepository :
    JpaRepository<ShopItem, ShopItemId>,
    ShopItemRepository {
    override fun findByIdOrNull(id: ShopItemId): ShopItem? = findById(id).orElse(null)
}
