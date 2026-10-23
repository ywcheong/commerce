package ywcheong.commerce.common.id

import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class UuidV7Test {
    @Test
    fun `UUID 버전 7 형식으로 생성된다`() {
        val uuid = UuidV7.generate()

        uuid.version() shouldBe 7
        uuid.variant() shouldBe 2
    }

    @Test
    fun `서로 다른 밀리초에 생성된 UUID는 생성 순서대로 정렬된다`() {
        val first = UuidV7.generate()
        Thread.sleep(2)
        val second = UuidV7.generate()

        first shouldBeLessThan second
    }
}
