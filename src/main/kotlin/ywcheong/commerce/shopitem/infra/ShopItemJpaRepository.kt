package ywcheong.commerce.shopitem.infra

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ywcheong.commerce.shopitem.domain.ShopItem
import ywcheong.commerce.shopitem.domain.ShopItemId
import ywcheong.commerce.shopitem.domain.ShopItemRepository

@Repository
interface ShopItemJpaRepository :
    JpaRepository<ShopItem, ShopItemId>,
    ShopItemRepository {
    override fun findByIdOrNull(id: ShopItemId): ShopItem? = findById(id).orElse(null)
}
