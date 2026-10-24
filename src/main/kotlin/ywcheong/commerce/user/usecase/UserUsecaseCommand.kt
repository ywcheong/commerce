package ywcheong.commerce.user.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.user.domain.User
import ywcheong.commerce.user.domain.UserForbiddenException
import ywcheong.commerce.user.domain.UserId
import ywcheong.commerce.user.domain.UserNotFoundException
import ywcheong.commerce.user.domain.UserRepository

@Service
@Transactional
class UserUsecaseCommand(
    private val userRepository: UserRepository,
) {
    fun createUser(): UserId = userRepository.save(User.create()).id

    fun deleteUser(
        currentUserId: UserId,
        userId: UserId,
    ) {
        if (currentUserId != userId) throw UserForbiddenException()
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        userRepository.delete(user)
    }
}
