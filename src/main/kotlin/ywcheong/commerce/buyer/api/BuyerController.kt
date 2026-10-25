package ywcheong.commerce.buyer.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.buyer.usecase.GetBuyer
import ywcheong.commerce.buyer.usecase.RegisterBuyer
import ywcheong.commerce.common.passport.CurrentIdentity
import ywcheong.commerce.identity.domain.IdentityId
import java.util.UUID

@RestController
class BuyerController(
    private val registerBuyer: RegisterBuyer,
    private val getBuyer: GetBuyer,
) {
    @PostMapping("/buyers")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerBuyer(
        @CurrentIdentity identityId: IdentityId,
    ): RegisterBuyerResponse = RegisterBuyerResponse(registerBuyer.execute(RegisterBuyer.Command(identityId)).buyerId.value)

    @GetMapping("/buyers/me")
    fun getMyBuyer(
        @CurrentBuyer buyerId: BuyerId,
    ): BuyerResponse {
        val result = getBuyer.execute(GetBuyer.Command(buyerId))
        return BuyerResponse(result.buyerId.value, result.identityId.value)
    }

    data class RegisterBuyerResponse(
        val buyerId: UUID,
    )

    data class BuyerResponse(
        val buyerId: UUID,
        val identityId: UUID,
    )
}
