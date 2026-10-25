package ywcheong.commerce.common.domain

import jakarta.persistence.Embeddable
import ywcheong.commerce.common.error.BusinessException

@Embeddable
data class Quantity(
    val value: Int,
) {
    init {
        if (value < 0) throw InvalidQuantityException()
    }

    operator fun compareTo(other: Quantity): Int = value.compareTo(other.value)

    operator fun minus(other: Quantity): Quantity = Quantity(value - other.value)
}

class InvalidQuantityException : BusinessException("INVALID_QUANTITY", "수량은 0 이상이어야 합니다.")
