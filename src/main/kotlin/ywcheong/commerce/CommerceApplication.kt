package ywcheong.commerce

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CommerceApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<CommerceApplication>(*args)
}
