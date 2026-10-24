package ywcheong.commerce.common.error

abstract class BusinessException(
    val code: String,
    val detail: String,
) : RuntimeException(detail)
