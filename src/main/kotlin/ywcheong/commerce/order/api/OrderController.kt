package ywcheong.commerce.order.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ywcheong.commerce.buyer.api.CurrentBuyer
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.common.domain.Quantity
import ywcheong.commerce.order.domain.OrderId
import ywcheong.commerce.order.usecase.GetOrder
import ywcheong.commerce.order.usecase.PlaceOrder
import ywcheong.commerce.shop.domain.ShopItemId
import java.util.UUID

@RestController
class OrderController(
    private val placeOrder: PlaceOrder,
    private val getOrder: GetOrder,
) {
    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    fun placeOrder(
        @CurrentBuyer buyerId: BuyerId,
        @RequestBody request: PlaceOrderRequest,
    ): PlaceOrderResponse {
        val placeCommand =
            PlaceOrder.Command(
                buyerId = buyerId,
                items =
                    request.items.map {
                        PlaceOrder.Command.Item(
                            shopItemId = ShopItemId(it.shopItemId),
                            quantity = Quantity(it.quantity),
                        )
                    },
            )
        val placeResult = placeOrder.execute(placeCommand)
        return PlaceOrderResponse(orderId = placeResult.orderId.value)
    }

    @GetMapping("/orders/{orderId}")
    fun getOrder(
        @CurrentBuyer buyerId: BuyerId,
        @PathVariable orderId: OrderId,
    ): OrderResponse {
        val getCommand =
            GetOrder.Command(
                buyerId = buyerId,
                orderId = orderId,
            )
        val getResult = getOrder.execute(getCommand)
        return OrderResponse(
            orderId = getResult.orderId.value,
            totalPrice = getResult.totalPrice,
            items =
                getResult.items.map {
                    OrderResponse.Item(
                        shopItemId = it.shopItemId.value,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                    )
                },
        )
    }

    data class PlaceOrderRequest(
        val items: List<Item>,
    ) {
        data class Item(
            val shopItemId: UUID,
            val quantity: Int,
        )
    }

    data class PlaceOrderResponse(
        val orderId: UUID,
    )

    data class OrderResponse(
        val orderId: UUID,
        val totalPrice: Long,
        val items: List<Item>,
    ) {
        data class Item(
            val shopItemId: UUID,
            val quantity: Int,
            val unitPrice: Long,
        )
    }
}
