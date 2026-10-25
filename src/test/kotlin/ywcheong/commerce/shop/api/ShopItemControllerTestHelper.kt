package ywcheong.commerce.shop.api

import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver.Companion.IDENTITY_HEADER
import ywcheong.commerce.errorCode

class ShopItemControllerTestHelper(
    mockMvc: MockMvc,
) {
    private val client = RestTestClient.bindTo(mockMvc).build()

    fun registerShopItem(
        identityId: String?,
        name: String = "키보드",
        price: Long = 10_000,
        stock: Int = 10,
    ): ShopItemResult {
        val result =
            client
                .post()
                .uri("/shops/items")
                .headers { if (identityId != null) it.set(IDENTITY_HEADER, identityId) }
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapOf("name" to name, "price" to price, "stock" to stock))
                .exchange()
                .returnResult<String>()
        val shopItemId: String? =
            if (result.status.is2xxSuccessful) JsonPath.read(result.responseBody, "\$.shopItemId") else null
        return ShopItemResult(
            status = result.status,
            shopItemId = shopItemId,
            body = null,
            code = errorCode(result.responseBody),
        )
    }

    fun findShopItem(shopItemId: String): ShopItemResult {
        val result =
            client
                .get()
                .uri("/shops/items/{shopItemId}", shopItemId)
                .exchange()
                .returnResult<String>()
        val shopItem: Map<String, Any?>? =
            if (result.status.is2xxSuccessful) JsonPath.read(result.responseBody, "\$") else null
        return ShopItemResult(
            status = result.status,
            shopItemId = shopItem?.get("shopItemId") as String?,
            body = shopItem,
            code = errorCode(result.responseBody),
        )
    }

    fun findShopItemIds(): List<String> {
        val result =
            client
                .get()
                .uri("/shops/items")
                .exchange()
                .returnResult<String>()
        check(result.status.value() == 200) { "상품 목록 조회 실패: ${result.status}" }
        return JsonPath.read(result.responseBody, "\$[*].shopItemId")
    }
}

data class ShopItemResult(
    val status: HttpStatusCode,
    val shopItemId: String?,
    val body: Map<String, Any?>?,
    val code: String?,
)
