package ywcheong.commerce.common.error

class NotFoundException(
    message: String,
) : RuntimeException(message)

class ForbiddenException(
    message: String,
) : RuntimeException(message)
