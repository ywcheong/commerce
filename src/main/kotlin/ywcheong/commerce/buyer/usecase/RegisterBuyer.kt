package ywcheong.commerce.buyer.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.buyer.domain.Buyer
import ywcheong.commerce.buyer.domain.BuyerAlreadyRegisteredException
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.buyer.domain.BuyerRepository
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.identity.domain.IdentityNotFoundException
import ywcheong.commerce.identity.domain.IdentityRepository

@Service
@Transactional
class RegisterBuyer(
    private val identityRepository: IdentityRepository,
    private val buyerRepository: BuyerRepository,
) {
    fun execute(command: Command): Result {
        identityRepository.findByIdOrNull(command.identityId) ?: throw IdentityNotFoundException()
        if (buyerRepository.findByIdentityId(command.identityId) != null) throw BuyerAlreadyRegisteredException()
        val buyer =
            Buyer(
                id = BuyerId.generate(),
                identityId = command.identityId,
            )
        return Result(buyerId = buyerRepository.save(buyer).id)
    }

    data class Command(
        val identityId: IdentityId,
    )

    data class Result(
        val buyerId: BuyerId,
    )
}
