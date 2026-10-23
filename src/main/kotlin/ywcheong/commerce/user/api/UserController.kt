package ywcheong.commerce.user.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ywcheong.commerce.common.passport.CurrentUser
import ywcheong.commerce.user.domain.UserId
import ywcheong.commerce.user.usecase.UserUsecaseCommand
import ywcheong.commerce.user.usecase.UserUsecaseQuery
import java.util.UUID

@RestController
class UserController(
    private val userUsecaseCommand: UserUsecaseCommand,
    private val userUsecaseQuery: UserUsecaseQuery,
) {
    @PostMapping("/api-public/users")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(): UserResponse = UserResponse(userUsecaseCommand.createUser().value)

    @GetMapping("/api/users/{userId}")
    fun findUser(
        @CurrentUser currentUserId: UserId,
        @PathVariable userId: UserId,
    ): UserResponse = UserResponse(userUsecaseQuery.findUser(currentUserId, userId).id.value)

    @DeleteMapping("/api/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(
        @CurrentUser currentUserId: UserId,
        @PathVariable userId: UserId,
    ) {
        userUsecaseCommand.deleteUser(currentUserId, userId)
    }

    data class UserResponse(
        val userId: UUID,
    )
}
