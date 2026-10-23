package ywcheong.commerce.user.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.common.error.ForbiddenException
import ywcheong.commerce.common.error.NotFoundException
import ywcheong.commerce.user.domain.User
import ywcheong.commerce.user.domain.UserId
import ywcheong.commerce.user.domain.UserRepository

@Service
@Transactional(readOnly = true)
class UserUsecaseQuery(
    private val userRepository: UserRepository,
) {
    fun findUser(
        currentUserId: UserId,
        userId: UserId,
    ): User {
        if (currentUserId != userId) throw ForbiddenException("다른 사용자를 조회할 수 없다")
        return userRepository.findByIdOrNull(userId) ?: throw NotFoundException("사용자가 없다")
    }
}
