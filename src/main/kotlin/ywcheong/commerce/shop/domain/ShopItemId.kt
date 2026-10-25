package ywcheong.commerce.shop.domain

import jakarta.persistence.Embeddable
import ywcheong.commerce.common.id.EntityId
import ywcheong.commerce.common.id.UuidV7
import java.util.UUID

@Embeddable
data class ShopItemId(
    override val value: UUID,
) : EntityId {
    constructor(value: String) : this(UUID.fromString(value))

    companion object {
        fun generate(): ShopItemId = ShopItemId(UuidV7.generate())
    }
}
