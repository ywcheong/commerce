package ywcheong.commerce.user.domain

import ywcheong.commerce.common.error.BusinessException

class UserNotFoundException : BusinessException("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.")

class UserForbiddenException : BusinessException("USER_FORBIDDEN", "권한이 없습니다.")
