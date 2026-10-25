package ywcheong.commerce.shopitem.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.shopitem.domain.ShopItemId
import ywcheong.commerce.shopitem.domain.ShopItemNotFoundException
import ywcheong.commerce.shopitem.domain.ShopItemRepository

@Service
@Transactional(readOnly = true)
class GetShopItem(
    private val shopItemRepository: ShopItemRepository,
) {
    fun execute(command: Command): Result {
        val shopItem = shopItemRepository.findByIdOrNull(command.shopItemId) ?: throw ShopItemNotFoundException()
        return Result(
            shopItemId = shopItem.id,
            sellerId = shopItem.sellerId,
            name = shopItem.name.value,
            price = shopItem.price.value,
            stock = shopItem.stock.value,
        )
    }

    data class Command(
        val shopItemId: ShopItemId,
    )

    data class Result(
        val shopItemId: ShopItemId,
        val sellerId: SellerId,
        val name: String,
        val price: Long,
        val stock: Int,
    )
}
