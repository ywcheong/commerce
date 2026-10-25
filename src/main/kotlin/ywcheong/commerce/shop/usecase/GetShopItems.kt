package ywcheong.commerce.shop.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.shop.domain.ShopItemId
import ywcheong.commerce.shop.domain.ShopItemRepository

@Service
@Transactional(readOnly = true)
class GetShopItems(
    private val shopItemRepository: ShopItemRepository,
) {
    fun execute(): Result =
        Result(
            shopItemRepository.findAll().map {
                Result.Item(
                    shopItemId = it.id,
                    sellerId = it.sellerId,
                    name = it.name.value,
                    price = it.price.value,
                    stock = it.stock.value,
                )
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
