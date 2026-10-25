package ywcheong.commerce.order.domain

import jakarta.persistence.AttributeOverride
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.common.domain.Money

@Entity
@Table(name = "orders")
class Order(
    id: OrderId,
    buyerId: BuyerId,
    items: List<OrderItem>,
) {
    init {
        if (items.isEmpty()) throw EmptyOrderException()
        if (items.distinctBy { it.shopItemId }.size != items.size) throw DuplicateOrderItemException()
    }

    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    var id: OrderId = id
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "buyer_id"))
    var buyerId: BuyerId = buyerId
        protected set

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    @OrderBy("id")
    var items: List<OrderItem> = items
        protected set

    fun totalPrice(): Money = items.map { it.price() }.reduce(Money::plus)

    fun isPlacedBy(buyerId: BuyerId): Boolean = this.buyerId == buyerId
}
