package ywcheong.commerce.identity.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.identity.domain.IdentityForbiddenException
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.identity.domain.IdentityNotFoundException
import ywcheong.commerce.identity.domain.IdentityRepository

@Service
@Transactional(readOnly = true)
class GetIdentity(
    private val identityRepository: IdentityRepository,
) {
    fun execute(command: Command): Result {
        if (command.currentIdentityId != command.identityId) throw IdentityForbiddenException()
        val identity = identityRepository.findByIdOrNull(command.identityId) ?: throw IdentityNotFoundException()
        return Result(identity.id)
    }

    data class Command(
        val currentIdentityId: IdentityId,
        val identityId: IdentityId,
    )

    data class Result(
        val identityId: IdentityId,
    )
}
