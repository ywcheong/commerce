package ywcheong.commerce.identity.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.identity.domain.Identity
import ywcheong.commerce.identity.domain.IdentityForbiddenException
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.identity.domain.IdentityNotFoundException
import ywcheong.commerce.identity.domain.IdentityRepository

@Service
@Transactional(readOnly = true)
class IdentityUsecaseQuery(
    private val identityRepository: IdentityRepository,
) {
    fun findIdentity(
        currentIdentityId: IdentityId,
        identityId: IdentityId,
    ): Identity {
        if (currentIdentityId != identityId) throw IdentityForbiddenException()
        return identityRepository.findByIdOrNull(identityId) ?: throw IdentityNotFoundException()
    }
}
