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
import ywcheong.commerce.shopitem.domain.ShopItemId
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
        val command = PlaceOrder.Command(buyerId, ShopItemId(request.shopItemId), Quantity(request.quantity))
        return PlaceOrderResponse(placeOrder.execute(command).orderId.value)
    }

    @GetMapping("/orders/{orderId}")
    fun getOrder(
        @CurrentBuyer buyerId: BuyerId,
        @PathVariable orderId: OrderId,
    ): OrderResponse {
        val result = getOrder.execute(GetOrder.Command(buyerId, orderId))
        return OrderResponse(result.orderId.value, result.shopItemId.value, result.quantity, result.totalPrice)
    }

    data class PlaceOrderRequest(
        val shopItemId: UUID,
        val quantity: Int,
    )

    data class PlaceOrderResponse(
        val orderId: UUID,
    )

    data class OrderResponse(
        val orderId: UUID,
        val shopItemId: UUID,
        val quantity: Int,
        val totalPrice: Long,
    )
}
