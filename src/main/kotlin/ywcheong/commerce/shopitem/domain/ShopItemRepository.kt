package ywcheong.commerce.shopitem.domain

interface ShopItemRepository {
    fun save(shopItem: ShopItem): ShopItem

    fun findByIdOrNull(id: ShopItemId): ShopItem?

    fun findAll(): List<ShopItem>
}
