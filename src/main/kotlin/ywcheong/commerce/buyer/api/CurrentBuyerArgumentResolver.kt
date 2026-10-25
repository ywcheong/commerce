package ywcheong.commerce.buyer.api

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import ywcheong.commerce.buyer.domain.BuyerId
import ywcheong.commerce.buyer.domain.BuyerRepository
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver

class CurrentBuyerArgumentResolver(
    private val buyerRepository: BuyerRepository,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(CurrentBuyer::class.java) && parameter.parameterType == BuyerId::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): BuyerId {
        val identityId = CurrentIdentityArgumentResolver.resolveIdentityId(webRequest)
        val buyer =
            buyerRepository.findByIdentityId(identityId)
                ?: throw ErrorResponseException(
                    HttpStatus.FORBIDDEN,
                    ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "구매자 등록이 필요합니다."),
                    null,
                )
        return buyer.id
    }
}
