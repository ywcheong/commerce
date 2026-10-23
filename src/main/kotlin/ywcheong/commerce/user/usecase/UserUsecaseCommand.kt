package ywcheong.commerce.user.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.common.error.ForbiddenException
import ywcheong.commerce.common.error.NotFoundException
import ywcheong.commerce.user.domain.User
import ywcheong.commerce.user.domain.UserId
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
        if (currentUserId != userId) throw ForbiddenException("다른 사용자를 삭제할 수 없다")
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException("사용자가 없다")
        userRepository.delete(user)
    }
}
