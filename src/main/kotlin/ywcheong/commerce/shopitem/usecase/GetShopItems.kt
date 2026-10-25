package ywcheong.commerce.shopitem.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.shopitem.domain.ShopItemId
import ywcheong.commerce.shopitem.domain.ShopItemRepository

@Service
@Transactional(readOnly = true)
class GetShopItems(
    private val shopItemRepository: ShopItemRepository,
) {
    fun execute(): Result =
        Result(
            shopItemRepository.findAll().map {
                Result.Item(it.id, it.sellerId, it.name.value, it.price.value, it.stock.value)
            },
        )

    data class Result(
        val items: List<Item>,
    ) {
        data class Item(
            val shopItemId: ShopItemId,
            val sellerId: SellerId,
            val name: String,
            val price: Long,
            val stock: Int,
        )
    }
}
