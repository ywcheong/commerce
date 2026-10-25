package ywcheong.commerce.common.domain

import jakarta.persistence.Embeddable
import ywcheong.commerce.common.error.BusinessException

@Embeddable
data class Money(
    val value: Long,
) {
    init {
        if (value < 0) throw InvalidMoneyException()
    }

    operator fun plus(other: Money): Money = Money(Math.addExact(value, other.value))

    operator fun times(quantity: Quantity): Money = Money(Math.multiplyExact(value, quantity.value.toLong()))
}

class InvalidMoneyException : BusinessException("INVALID_MONEY", "금액은 0 이상이어야 합니다.")
