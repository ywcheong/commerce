package ywcheong.commerce.identity.domain

import ywcheong.commerce.common.error.BusinessException

class IdentityNotFoundException : BusinessException("IDENTITY_NOT_FOUND", "사용자를 찾을 수 없습니다.")

class IdentityForbiddenException : BusinessException("IDENTITY_FORBIDDEN", "권한이 없습니다.")
