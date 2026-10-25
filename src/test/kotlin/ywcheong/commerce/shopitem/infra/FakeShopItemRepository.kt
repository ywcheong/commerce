package ywcheong.commerce.shopitem.infra

import ywcheong.commerce.shopitem.domain.ShopItem
import ywcheong.commerce.shopitem.domain.ShopItemId
import ywcheong.commerce.shopitem.domain.ShopItemRepository
import java.util.concurrent.ConcurrentHashMap

class FakeShopItemRepository : ShopItemRepository {
    private val shopItems = ConcurrentHashMap<ShopItemId, ShopItem>()

    override fun save(shopItem: ShopItem): ShopItem {
        shopItems[shopItem.id] = shopItem
        return shopItem
    }

    override fun findByIdOrNull(id: ShopItemId): ShopItem? = shopItems[id]

    override fun findAll(): List<ShopItem> = shopItems.values.toList()
}
