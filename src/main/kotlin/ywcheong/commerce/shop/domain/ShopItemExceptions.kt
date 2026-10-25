package ywcheong.commerce.shop.domain

import ywcheong.commerce.common.error.BusinessException

class InvalidShopItemNameException : BusinessException("INVALID_SHOP_ITEM_NAME", "상품 이름은 1자 이상 ${ShopItemName.MAX_LENGTH}자 이하여야 합니다.")

class ShopItemNotFoundException : BusinessException("SHOP_ITEM_NOT_FOUND", "상품을 찾을 수 없습니다.")

class OutOfStockException : BusinessException("OUT_OF_STOCK", "재고가 부족합니다.")
