package ywcheong.commerce.identity.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.identity.domain.Identity
import ywcheong.commerce.identity.domain.IdentityForbiddenException
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.identity.domain.IdentityNotFoundException
import ywcheong.commerce.identity.domain.IdentityRepository

@Service
@Transactional
class IdentityUsecaseCommand(
    private val identityRepository: IdentityRepository,
) {
    fun createIdentity(): IdentityId = identityRepository.save(Identity.create()).id

    fun deleteIdentity(
        currentIdentityId: IdentityId,
        identityId: IdentityId,
    ) {
        if (currentIdentityId != identityId) throw IdentityForbiddenException()
        val identity = identityRepository.findByIdOrNull(identityId) ?: throw IdentityNotFoundException()
        identityRepository.delete(identity)
    }
}
