package ywcheong.commerce.order.domain

interface OrderRepository {
    fun save(order: Order): Order

    fun findByIdOrNull(id: OrderId): Order?
}
