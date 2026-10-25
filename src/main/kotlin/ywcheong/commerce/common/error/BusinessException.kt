package ywcheong.commerce.common.error

@Suppress("AbstractClassCanBeConcreteClass")
abstract class BusinessException(
    val code: String,
    val detail: String,
) : RuntimeException(detail)
