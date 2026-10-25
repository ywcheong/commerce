package ywcheong.commerce.seller.domain

import jakarta.persistence.Embeddable
import ywcheong.commerce.common.id.EntityId
import ywcheong.commerce.common.id.UuidV7
import java.util.UUID

@Embeddable
data class SellerId(
    override val value: UUID,
) : EntityId {
    constructor(value: String) : this(UUID.fromString(value))

    companion object {
        fun generate(): SellerId = SellerId(UuidV7.generate())
    }
}
