package ywcheong.commerce.shopitem.domain

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import ywcheong.commerce.common.domain.Money
import ywcheong.commerce.common.domain.Quantity
import ywcheong.commerce.seller.domain.SellerId

@Entity
@Table(name = "shop_items")
class ShopItem(
    id: ShopItemId,
    sellerId: SellerId,
    name: ShopItemName,
    price: Money,
    stock: Quantity,
) {
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    var id: ShopItemId = id
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "seller_id"))
    var sellerId: SellerId = sellerId
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "name"))
    var name: ShopItemName = name
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "price"))
    var price: Money = price
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "stock"))
    var stock: Quantity = stock
        protected set

    fun decreaseStock(quantity: Quantity) {
        if (stock < quantity) throw OutOfStockException()
        stock -= quantity
    }
}
