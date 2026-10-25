package ywcheong.commerce.buyer.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.buyer.domain.BuyerNotFoundException
import ywcheong.commerce.buyer.domain.BuyerRepository
import ywcheong.commerce.identity.domain.IdentityId

@Service
@Transactional(readOnly = true)
class GetBuyer(
    private val buyerRepository: BuyerRepository,
) {
    fun execute(command: Command): Result {
        val buyer = buyerRepository.findByIdOrNull(command.buyerId) ?: throw BuyerNotFoundException()
        return Result(buyer.id, buyer.identityId)
    }

    data class Command(
        val buyerId: BuyerId,
    )

    data class Result(
        val buyerId: BuyerId,
        val identityId: IdentityId,
    )
}
