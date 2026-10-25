package ywcheong.commerce.identity.domain

import jakarta.persistence.Embeddable
import ywcheong.commerce.common.id.UuidV7
import java.util.UUID

@Embeddable
data class IdentityId(
    val value: UUID,
) {
    constructor(value: String) : this(UUID.fromString(value))

    companion object {
        fun generate(): IdentityId = IdentityId(UuidV7.generate())
    }
}
