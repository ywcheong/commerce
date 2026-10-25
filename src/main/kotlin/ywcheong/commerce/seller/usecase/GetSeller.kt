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
        val seller = sellerRepository.findByIdOrNull(command.sellerId) ?: throw SellerNotFoundException()
        return Result(seller.id, seller.identityId)
    }

    data class Command(
        val sellerId: SellerId,
    )

    data class Result(
        val sellerId: SellerId,
        val identityId: IdentityId,
    )
}
