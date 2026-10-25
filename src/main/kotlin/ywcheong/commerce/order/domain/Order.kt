package ywcheong.commerce.order.domain

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.common.domain.Money
import ywcheong.commerce.common.domain.Quantity
import ywcheong.commerce.shopitem.domain.ShopItemId

@Entity
@Table(name = "orders")
class Order(
    id: OrderId,
    buyerId: BuyerId,
    shopItemId: ShopItemId,
    quantity: Quantity,
    totalPrice: Money,
) {
    init {
        if (quantity.value == 0) throw InvalidOrderQuantityException()
    }

    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    var id: OrderId = id
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "buyer_id"))
    var buyerId: BuyerId = buyerId
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
    @AttributeOverride(name = "value", column = Column(name = "total_price"))
    var totalPrice: Money = totalPrice
        protected set

    fun isPlacedBy(buyerId: BuyerId): Boolean = this.buyerId == buyerId
}
