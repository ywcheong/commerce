package ywcheong.commerce.shopitem.domain

import jakarta.persistence.Embeddable
import ywcheong.commerce.common.id.UuidV7
import java.util.UUID

@Embeddable
data class ShopItemId(
    val value: UUID,
) {
    constructor(value: String) : this(UUID.fromString(value))

    companion object {
        fun generate(): ShopItemId = ShopItemId(UuidV7.generate())
    }
}
