package ywcheong.commerce.order.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.common.domain.Quantity
import ywcheong.commerce.order.domain.Order
import ywcheong.commerce.order.domain.OrderId
import ywcheong.commerce.order.domain.OrderItem
import ywcheong.commerce.order.domain.OrderItemId
import ywcheong.commerce.order.domain.OrderRepository
import ywcheong.commerce.shop.domain.ShopItemId
import ywcheong.commerce.shop.domain.ShopItemNotFoundException
import ywcheong.commerce.shop.domain.ShopItemRepository

@Service
@Transactional
class PlaceOrder(
    private val shopItemRepository: ShopItemRepository,
    private val orderRepository: OrderRepository,
) {
    fun execute(command: Command): Result {
        val orderItems =
            command.items.map { item ->
                val shopItem = shopItemRepository.findByIdOrNull(item.shopItemId) ?: throw ShopItemNotFoundException()
                val orderItem =
                    OrderItem(
                        id = OrderItemId.generate(),
                        shopItemId = shopItem.id,
                        quantity = item.quantity,
                        unitPrice = shopItem.price,
                    )
                shopItem.decreaseStock(item.quantity)
                orderItem
            }
        val order =
            Order(
                id = OrderId.generate(),
                buyerId = command.buyerId,
                items = orderItems,
            )
        return Result(orderId = orderRepository.save(order).id)
    }

    data class Command(
        val buyerId: BuyerId,
        val items: List<Item>,
    ) {
        data class Item(
            val shopItemId: ShopItemId,
            val quantity: Quantity,
        )
    }

    data class Result(
        val orderId: OrderId,
    )
}
