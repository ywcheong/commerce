package ywcheong.commerce.order.infra

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ywcheong.commerce.order.domain.Order
import ywcheong.commerce.order.domain.OrderId
import ywcheong.commerce.order.domain.OrderRepository

@Repository
interface OrderJpaRepository :
    JpaRepository<Order, OrderId>,
    OrderRepository {
    override fun findByIdOrNull(id: OrderId): Order? = findById(id).orElse(null)
}
