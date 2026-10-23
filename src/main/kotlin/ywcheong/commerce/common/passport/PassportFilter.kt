package ywcheong.commerce.common.passport

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ywcheong.commerce.user.domain.UserId

@Component
class PassportFilter : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean = !request.requestURI.startsWith("/api/")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val header = request.getHeader(USER_ID_HEADER)
        if (header == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            return
        }
        val userId =
            try {
                UserId(header)
            } catch (_: IllegalArgumentException) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                return
            }
        request.setAttribute(CURRENT_USER_ATTRIBUTE, userId)
        filterChain.doFilter(request, response)
    }

    companion object {
        const val USER_ID_HEADER = "X-User-Id"
        const val CURRENT_USER_ATTRIBUTE = "currentUser"
    }
}
