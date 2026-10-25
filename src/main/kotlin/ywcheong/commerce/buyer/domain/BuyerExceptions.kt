package ywcheong.commerce.buyer.domain

import ywcheong.commerce.common.error.BusinessException

class BuyerAlreadyRegisteredException : BusinessException("BUYER_ALREADY_REGISTERED", "이미 구매자로 등록되었습니다.")

class BuyerNotFoundException : BusinessException("BUYER_NOT_FOUND", "구매자를 찾을 수 없습니다.")
