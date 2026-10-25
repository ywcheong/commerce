package ywcheong.commerce.seller.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ywcheong.commerce.common.passport.CurrentIdentity
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.seller.usecase.GetSeller
import ywcheong.commerce.seller.usecase.RegisterSeller
import java.util.UUID

@RestController
class SellerController(
    private val registerSeller: RegisterSeller,
    private val getSeller: GetSeller,
) {
    @PostMapping("/sellers")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerSeller(
        @CurrentIdentity identityId: IdentityId,
    ): RegisterSellerResponse = RegisterSellerResponse(registerSeller.execute(RegisterSeller.Command(identityId)).sellerId.value)

    @GetMapping("/sellers/me")
    fun getMySeller(
        @CurrentSeller sellerId: SellerId,
    ): SellerResponse {
        val result = getSeller.execute(GetSeller.Command(sellerId))
        return SellerResponse(result.sellerId.value, result.identityId.value)
    }

    data class RegisterSellerResponse(
        val sellerId: UUID,
    )

    data class SellerResponse(
        val sellerId: UUID,
        val identityId: UUID,
    )
}
