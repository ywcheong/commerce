package ywcheong.commerce.identity.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.identity.domain.Identity
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.identity.domain.IdentityRepository

@Service
@Transactional
class CreateIdentity(
    private val identityRepository: IdentityRepository,
) {
    fun execute(): Result = Result(identityRepository.save(Identity(IdentityId.generate())).id)

    data class Result(
        val identityId: IdentityId,
    )
}
