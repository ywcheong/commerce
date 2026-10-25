package ywcheong.commerce.common.passport

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
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
    ): IdentityId =
        checkNotNull(
            webRequest.getAttribute(PassportFilter.CURRENT_IDENTITY_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST) as IdentityId?,
        ) { "@CurrentIdentity는 /api 경로에서만 사용할 수 있다" }
}
