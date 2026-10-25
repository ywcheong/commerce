package ywcheong.commerce.order.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.order.domain.OrderForbiddenException
import ywcheong.commerce.order.domain.OrderId
import ywcheong.commerce.order.domain.OrderNotFoundException
import ywcheong.commerce.order.domain.OrderRepository
import ywcheong.commerce.shop.domain.ShopItemId

@Service
@Transactional(readOnly = true)
class GetOrder(
    private val orderRepository: OrderRepository,
) {
    fun execute(command: Command): Result {
        val order = orderRepository.findByIdOrNull(command.orderId) ?: throw OrderNotFoundException()
        if (!order.isPlacedBy(command.buyerId)) throw OrderForbiddenException()
        return Result(
            orderId = order.id,
            shopItemId = order.shopItemId,
            quantity = order.quantity.value,
            totalPrice = order.totalPrice.value,
        )
    }

    data class Command(
        val buyerId: BuyerId,
        val orderId: OrderId,
    )

    data class Result(
        val orderId: OrderId,
        val shopItemId: ShopItemId,
        val quantity: Int,
        val totalPrice: Long,
    )
}
