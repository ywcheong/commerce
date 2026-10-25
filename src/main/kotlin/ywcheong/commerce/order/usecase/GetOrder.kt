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
            totalPrice = order.totalPrice().value,
            items =
                order.items.map {
                    Result.Item(
                        shopItemId = it.shopItemId,
                        quantity = it.quantity.value,
                        unitPrice = it.unitPrice.value,
                    )
                },
        )
    }

    data class Command(
        val buyerId: BuyerId,
        val orderId: OrderId,
    )

    data class Result(
        val orderId: OrderId,
        val totalPrice: Long,
        val items: List<Item>,
    ) {
        data class Item(
            val shopItemId: ShopItemId,
            val quantity: Int,
            val unitPrice: Long,
        )
    }
}
