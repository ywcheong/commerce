package ywcheong.commerce.buyer.domain

import jakarta.persistence.Embeddable
import ywcheong.commerce.common.id.UuidV7
import java.util.UUID

@Embeddable
data class BuyerId(
    val value: UUID,
) {
    constructor(value: String) : this(UUID.fromString(value))

    companion object {
        fun generate(): BuyerId = BuyerId(UuidV7.generate())
    }
}
