package ywcheong.commerce.shopitem.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ywcheong.commerce.common.domain.Money
import ywcheong.commerce.common.domain.Quantity
import ywcheong.commerce.seller.api.CurrentSeller
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.shopitem.domain.ShopItemId
import ywcheong.commerce.shopitem.domain.ShopItemName
import ywcheong.commerce.shopitem.usecase.GetShopItem
import ywcheong.commerce.shopitem.usecase.GetShopItems
import ywcheong.commerce.shopitem.usecase.RegisterShopItem
import java.util.UUID

@RestController
class ShopItemController(
    private val registerShopItem: RegisterShopItem,
    private val getShopItem: GetShopItem,
    private val getShopItems: GetShopItems,
) {
    @PostMapping("/shop-items")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerShopItem(
        @CurrentSeller sellerId: SellerId,
        @RequestBody request: RegisterShopItemRequest,
    ): RegisterShopItemResponse {
        val command =
            RegisterShopItem.Command(sellerId, ShopItemName(request.name), Money(request.price), Quantity(request.stock))
        return RegisterShopItemResponse(registerShopItem.execute(command).shopItemId.value)
    }

    @GetMapping("/shop-items")
    fun getShopItems(): List<ShopItemResponse> =
        getShopItems.execute().items.map {
            ShopItemResponse(it.shopItemId.value, it.sellerId.value, it.name, it.price, it.stock)
        }

    @GetMapping("/shop-items/{shopItemId}")
    fun getShopItem(
        @PathVariable shopItemId: ShopItemId,
    ): ShopItemResponse {
        val result = getShopItem.execute(GetShopItem.Command(shopItemId))
        return ShopItemResponse(result.shopItemId.value, result.sellerId.value, result.name, result.price, result.stock)
    }

    data class RegisterShopItemRequest(
        val name: String,
        val price: Long,
        val stock: Int,
    )

    data class RegisterShopItemResponse(
        val shopItemId: UUID,
    )

    data class ShopItemResponse(
        val shopItemId: UUID,
        val sellerId: UUID,
        val name: String,
        val price: Long,
        val stock: Int,
    )
}
