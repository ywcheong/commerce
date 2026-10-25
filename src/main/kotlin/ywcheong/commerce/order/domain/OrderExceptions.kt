package ywcheong.commerce.order.domain

import ywcheong.commerce.common.error.BusinessException

class InvalidOrderQuantityException : BusinessException("INVALID_ORDER_QUANTITY", "주문 수량은 1 이상이어야 합니다.")

class OrderNotFoundException : BusinessException("ORDER_NOT_FOUND", "주문을 찾을 수 없습니다.")

class OrderForbiddenException : BusinessException("ORDER_FORBIDDEN", "권한이 없습니다.")

class EmptyOrderException : BusinessException("EMPTY_ORDER", "주문 상품이 1개 이상이어야 합니다.")

class DuplicateOrderItemException : BusinessException("DUPLICATE_ORDER_ITEM", "같은 상품을 중복으로 주문할 수 없습니다.")
