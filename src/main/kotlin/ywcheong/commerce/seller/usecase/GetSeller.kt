package ywcheong.commerce.seller.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.seller.domain.SellerNotFoundException
import ywcheong.commerce.seller.domain.SellerRepository

@Service
@Transactional(readOnly = true)
class GetSeller(
    private val sellerRepository: SellerRepository,
) {
    fun execute(command: Command): Result {
        val seller =
            when (command) {
                is Command.BySellerId -> sellerRepository.findByIdOrNull(command.sellerId)
                is Command.ByIdentityId -> sellerRepository.findByIdentityId(command.identityId)
            } ?: throw SellerNotFoundException()
        return Result(
            sellerId = seller.id,
            identityId = seller.identityId,
        )
    }

    sealed interface Command {
        data class BySellerId(
            val sellerId: SellerId,
        ) : Command

        data class ByIdentityId(
            val identityId: IdentityId,
        ) : Command
    }

    data class Result(
        val sellerId: SellerId,
        val identityId: IdentityId,
    )
}
