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
import ywcheong.commerce.user.domain.UserId

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
        val header = request.getHeader(USER_ID_HEADER)
        if (header == null) {
            handlerExceptionResolver.resolveException(request, response, null, unauthenticated())
            return
        }
        val userId =
            try {
                UserId(header)
            } catch (_: IllegalArgumentException) {
                handlerExceptionResolver.resolveException(request, response, null, unauthenticated())
                return
            }
        request.setAttribute(CURRENT_USER_ATTRIBUTE, userId)
        filterChain.doFilter(request, response)
    }

    private fun unauthenticated() =
        ErrorResponseException(
            HttpStatus.UNAUTHORIZED,
            ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
            null,
        )

    companion object {
        const val USER_ID_HEADER = "X-User-Id"
        const val CURRENT_USER_ATTRIBUTE = "currentUser"
    }
}
