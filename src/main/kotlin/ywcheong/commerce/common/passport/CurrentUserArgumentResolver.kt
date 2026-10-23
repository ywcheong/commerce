package ywcheong.commerce.common.passport

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import ywcheong.commerce.user.domain.UserId

class CurrentUserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(CurrentUser::class.java) && parameter.parameterType == UserId::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): UserId =
        checkNotNull(
            webRequest.getAttribute(PassportFilter.CURRENT_USER_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST) as UserId?,
        ) { "@CurrentUser는 /api 경로에서만 사용할 수 있다" }
}
