package ywcheong.commerce.health

import ywcheong.commerce.common.error.BusinessException

class ExampleBusinessException : BusinessException("EXAMPLE_BUSINESS_ERROR", "비즈니스 오류 예시입니다.")
