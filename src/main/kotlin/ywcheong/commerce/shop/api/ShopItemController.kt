package ywcheong.commerce.shop.api

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
import ywcheong.commerce.shop.domain.ShopItemId
import ywcheong.commerce.shop.domain.ShopItemName
import ywcheong.commerce.shop.usecase.GetShopItem
import ywcheong.commerce.shop.usecase.GetShopItems
import ywcheong.commerce.shop.usecase.RegisterShopItem
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
        val registerCommand =
            RegisterShopItem.Command(
                sellerId = sellerId,
                name = ShopItemName(request.name),
                price = Money(request.price),
                stock = Quantity(request.stock),
            )
        val registerResult = registerShopItem.execute(registerCommand)
        return RegisterShopItemResponse(shopItemId = registerResult.shopItemId.value)
    }

    @GetMapping("/shop-items")
    fun getShopItems(): List<ShopItemResponse> {
        val getResult = getShopItems.execute()
        return getResult.items.map {
            ShopItemResponse(
                shopItemId = it.shopItemId.value,
                sellerId = it.sellerId.value,
                name = it.name,
                price = it.price,
                stock = it.stock,
            )
        }
    }

    @GetMapping("/shop-items/{shopItemId}")
    fun getShopItem(
        @PathVariable shopItemId: ShopItemId,
    ): ShopItemResponse {
        val getCommand = GetShopItem.Command(shopItemId = shopItemId)
        val getResult = getShopItem.execute(getCommand)
        return ShopItemResponse(
            shopItemId = getResult.shopItemId.value,
            sellerId = getResult.sellerId.value,
            name = getResult.name,
            price = getResult.price,
            stock = getResult.stock,
        )
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
