package ywcheong.commerce.user.domain

import jakarta.persistence.Embeddable
import ywcheong.commerce.common.id.UuidV7
import java.util.UUID

@Embeddable
data class UserId(
    val value: UUID,
) {
    constructor(value: String) : this(UUID.fromString(value))

    companion object {
        fun generate(): UserId = UserId(UuidV7.generate())
    }
}
