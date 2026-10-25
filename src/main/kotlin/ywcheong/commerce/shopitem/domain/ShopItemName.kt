package ywcheong.commerce.shopitem.domain

import jakarta.persistence.Embeddable

@Embeddable
data class ShopItemName(
    val value: String,
) {
    init {
        if (value.isBlank() || value.length > MAX_LENGTH) throw InvalidShopItemNameException()
    }

    companion object {
        const val MAX_LENGTH = 100
    }
}
