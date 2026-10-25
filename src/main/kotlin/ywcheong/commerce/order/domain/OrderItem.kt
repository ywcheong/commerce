package ywcheong.commerce.order.domain

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import ywcheong.commerce.common.domain.Money
import ywcheong.commerce.common.domain.Quantity
import ywcheong.commerce.shop.domain.ShopItemId

@Entity
@Table(name = "order_items")
class OrderItem(
    id: OrderItemId,
    shopItemId: ShopItemId,
    quantity: Quantity,
    unitPrice: Money,
) {
    init {
        if (quantity.value == 0) throw InvalidOrderQuantityException()
    }

    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    var id: OrderItemId = id
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "shop_item_id"))
    var shopItemId: ShopItemId = shopItemId
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "quantity"))
    var quantity: Quantity = quantity
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "unit_price"))
    var unitPrice: Money = unitPrice
        protected set

    fun price(): Money = unitPrice * quantity
}
