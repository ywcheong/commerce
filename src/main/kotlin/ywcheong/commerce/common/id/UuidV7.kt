package ywcheong.commerce.common.id

import java.security.SecureRandom
import java.util.UUID

object UuidV7 {
    private val random = SecureRandom()

    fun generate(): UUID {
        val msb = (System.currentTimeMillis() shl 16) or 0x7000L or (random.nextLong() and 0x0FFFL)
        val lsb = (random.nextLong() and 0x3FFF_FFFF_FFFF_FFFFL) or Long.MIN_VALUE
        return UUID(msb, lsb)
    }
}
