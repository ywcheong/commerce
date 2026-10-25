package ywcheong.commerce.order.infra

import ywcheong.commerce.order.domain.Order
import ywcheong.commerce.order.domain.OrderId
import ywcheong.commerce.order.domain.OrderRepository
import java.util.concurrent.ConcurrentHashMap

class FakeOrderRepository : OrderRepository {
    private val orders = ConcurrentHashMap<OrderId, Order>()

    override fun save(order: Order): Order {
        orders[order.id] = order
        return order
    }

    override fun findByIdOrNull(id: OrderId): Order? = orders[id]
}
