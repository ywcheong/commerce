package ywcheong.commerce.seller.domain

import ywcheong.commerce.common.error.BusinessException

class SellerAlreadyRegisteredException : BusinessException("SELLER_ALREADY_REGISTERED", "이미 판매자로 등록되었습니다.")

class SellerNotFoundException : BusinessException("SELLER_NOT_FOUND", "판매자를 찾을 수 없습니다.")
