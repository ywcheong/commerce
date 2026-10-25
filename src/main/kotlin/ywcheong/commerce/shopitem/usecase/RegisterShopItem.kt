package ywcheong.commerce.shopitem.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.common.domain.Money
import ywcheong.commerce.common.domain.Quantity
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.shopitem.domain.ShopItem
import ywcheong.commerce.shopitem.domain.ShopItemId
import ywcheong.commerce.shopitem.domain.ShopItemName
import ywcheong.commerce.shopitem.domain.ShopItemRepository

@Service
@Transactional
class RegisterShopItem(
    private val shopItemRepository: ShopItemRepository,
) {
    fun execute(command: Command): Result {
        val shopItem = ShopItem.register(command.sellerId, command.name, command.price, command.stock)
        return Result(shopItemRepository.save(shopItem).id)
    }

    data class Command(
        val sellerId: SellerId,
        val name: ShopItemName,
        val price: Money,
        val stock: Quantity,
    )

    data class Result(
        val shopItemId: ShopItemId,
    )
}
