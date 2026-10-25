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
import ywcheong.commerce.identity.usecase.IdentityUsecaseCommand
import ywcheong.commerce.identity.usecase.IdentityUsecaseQuery
import java.util.UUID

@RestController
class IdentityController(
    private val identityUsecaseCommand: IdentityUsecaseCommand,
    private val identityUsecaseQuery: IdentityUsecaseQuery,
) {
    @PostMapping("/api-public/identities")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIdentity(): IdentityResponse = IdentityResponse(identityUsecaseCommand.createIdentity().value)

    @GetMapping("/api/identities/{identityId}")
    fun findIdentity(
        @CurrentIdentity currentIdentityId: IdentityId,
        @PathVariable identityId: IdentityId,
    ): IdentityResponse = IdentityResponse(identityUsecaseQuery.findIdentity(currentIdentityId, identityId).id.value)

    @DeleteMapping("/api/identities/{identityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteIdentity(
        @CurrentIdentity currentIdentityId: IdentityId,
        @PathVariable identityId: IdentityId,
    ) {
        identityUsecaseCommand.deleteIdentity(currentIdentityId, identityId)
    }

    data class IdentityResponse(
        val identityId: UUID,
    )
}
