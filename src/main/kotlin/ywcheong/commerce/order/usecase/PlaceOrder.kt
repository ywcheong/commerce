package ywcheong.commerce.order.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.common.domain.Quantity
import ywcheong.commerce.order.domain.Order
import ywcheong.commerce.order.domain.OrderId
import ywcheong.commerce.order.domain.OrderRepository
import ywcheong.commerce.shopitem.domain.ShopItemId
import ywcheong.commerce.shopitem.domain.ShopItemNotFoundException
import ywcheong.commerce.shopitem.domain.ShopItemRepository

@Service
@Transactional
class PlaceOrder(
    private val shopItemRepository: ShopItemRepository,
    private val orderRepository: OrderRepository,
) {
    fun execute(command: Command): Result {
        val shopItem = shopItemRepository.findByIdOrNull(command.shopItemId) ?: throw ShopItemNotFoundException()
        val order = Order.place(command.buyerId, shopItem, command.quantity)
        return Result(orderRepository.save(order).id)
    }

    data class Command(
        val buyerId: BuyerId,
        val shopItemId: ShopItemId,
        val quantity: Quantity,
    )

    data class Result(
        val orderId: OrderId,
    )
}
