package ywcheong.commerce.common.passport

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import org.springframework.web.ErrorResponseException
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import ywcheong.commerce.identity.domain.IdentityId

@Component
class PassportFilter(
    @Qualifier("handlerExceptionResolver") private val handlerExceptionResolver: HandlerExceptionResolver,
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean = !request.requestURI.startsWith("/api/")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val header = request.getHeader(IDENTITY_HEADER)
        if (header == null) {
            handlerExceptionResolver.resolveException(request, response, null, unauthenticated())
            return
        }
        val identityId =
            try {
                IdentityId(header)
            } catch (_: IllegalArgumentException) {
                handlerExceptionResolver.resolveException(request, response, null, unauthenticated())
                return
            }
        request.setAttribute(CURRENT_IDENTITY_ATTRIBUTE, identityId)
        filterChain.doFilter(request, response)
    }

    private fun unauthenticated() =
        ErrorResponseException(
            HttpStatus.UNAUTHORIZED,
            ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
            null,
        )

    companion object {
        const val IDENTITY_HEADER = "X-Identity"
        const val CURRENT_IDENTITY_ATTRIBUTE = "currentIdentity"
    }
}
