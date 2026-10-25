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
        val buyer =
            when (command) {
                is Command.ByBuyerId -> buyerRepository.findByIdOrNull(command.buyerId)
                is Command.ByIdentityId -> buyerRepository.findByIdentityId(command.identityId)
            } ?: throw BuyerNotFoundException()
        return Result(
            buyerId = buyer.id,
            identityId = buyer.identityId,
        )
    }

    sealed interface Command {
        data class ByBuyerId(
            val buyerId: BuyerId,
        ) : Command

        data class ByIdentityId(
            val identityId: IdentityId,
        ) : Command
    }

    data class Result(
        val buyerId: BuyerId,
        val identityId: IdentityId,
    )
}
