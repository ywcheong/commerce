package ywcheong.commerce.user.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.user.domain.User
import ywcheong.commerce.user.domain.UserForbiddenException
import ywcheong.commerce.user.domain.UserId
import ywcheong.commerce.user.domain.UserNotFoundException
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
        if (currentUserId != userId) throw UserForbiddenException()
        return userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
    }
}
