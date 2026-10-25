package ywcheong.commerce.shop.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.common.domain.Money
import ywcheong.commerce.common.domain.Quantity
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.shop.domain.ShopItem
import ywcheong.commerce.shop.domain.ShopItemId
import ywcheong.commerce.shop.domain.ShopItemName
import ywcheong.commerce.shop.domain.ShopItemRepository

@Service
@Transactional
class RegisterShopItem(
    private val shopItemRepository: ShopItemRepository,
) {
    fun execute(command: Command): Result {
        val shopItem =
            ShopItem(
                id = ShopItemId.generate(),
                sellerId = command.sellerId,
                name = command.name,
                price = command.price,
                stock = command.stock,
            )
        return Result(shopItemId = shopItemRepository.save(shopItem).id)
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
