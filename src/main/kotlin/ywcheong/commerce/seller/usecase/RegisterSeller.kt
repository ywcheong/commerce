package ywcheong.commerce.seller.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.identity.domain.IdentityNotFoundException
import ywcheong.commerce.identity.domain.IdentityRepository
import ywcheong.commerce.seller.domain.Seller
import ywcheong.commerce.seller.domain.SellerAlreadyRegisteredException
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.seller.domain.SellerRepository

@Service
@Transactional
class RegisterSeller(
    private val identityRepository: IdentityRepository,
    private val sellerRepository: SellerRepository,
) {
    fun execute(command: Command): Result {
        identityRepository.findByIdOrNull(command.identityId) ?: throw IdentityNotFoundException()
        if (sellerRepository.findByIdentityId(command.identityId) != null) throw SellerAlreadyRegisteredException()
        return Result(sellerRepository.save(Seller(SellerId.generate(), command.identityId)).id)
    }

    data class Command(
        val identityId: IdentityId,
    )

    data class Result(
        val sellerId: SellerId,
    )
}
