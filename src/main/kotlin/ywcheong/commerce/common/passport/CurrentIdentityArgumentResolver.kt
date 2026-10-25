package ywcheong.commerce.common.passport

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import ywcheong.commerce.identity.domain.IdentityId

class CurrentIdentityArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(CurrentIdentity::class.java) && parameter.parameterType == IdentityId::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): IdentityId = resolveIdentityId(webRequest)

    companion object {
        const val IDENTITY_HEADER = "X-Identity"

        fun resolveIdentityId(webRequest: NativeWebRequest): IdentityId {
            val header = webRequest.getHeader(IDENTITY_HEADER) ?: throw unauthenticated()
            return try {
                IdentityId(header)
            } catch (_: IllegalArgumentException) {
                throw unauthenticated()
            }
        }

        private fun unauthenticated() =
            ErrorResponseException(
                HttpStatus.UNAUTHORIZED,
                ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
                null,
            )
    }
}
