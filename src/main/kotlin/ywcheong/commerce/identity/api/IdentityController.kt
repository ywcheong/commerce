package ywcheong.commerce.identity.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ywcheong.commerce.common.passport.CurrentIdentity
import ywcheong.commerce.identity.domain.IdentityId
import ywcheong.commerce.identity.usecase.CreateIdentity
import ywcheong.commerce.identity.usecase.DeleteIdentity
import ywcheong.commerce.identity.usecase.GetIdentity
import java.util.UUID

@RestController
class IdentityController(
    private val getIdentity: GetIdentity,
    private val createIdentity: CreateIdentity,
    private val deleteIdentity: DeleteIdentity,
) {
    @PostMapping("/api-public/identities")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIdentity(): IdentityResponse = IdentityResponse(createIdentity.execute().identityId.value)

    @GetMapping("/api/identities/{identityId}")
    fun getIdentity(
        @CurrentIdentity currentIdentityId: IdentityId,
        @PathVariable identityId: IdentityId,
    ): IdentityResponse {
        val result = getIdentity.execute(GetIdentity.Command(currentIdentityId, identityId))
        return IdentityResponse(result.identityId.value)
    }

    @DeleteMapping("/api/identities/{identityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteIdentity(
        @CurrentIdentity currentIdentityId: IdentityId,
        @PathVariable identityId: IdentityId,
    ) {
        deleteIdentity.execute(DeleteIdentity.Command(currentIdentityId, identityId))
    }

    data class IdentityResponse(
        val identityId: UUID,
    )
}
