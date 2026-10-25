package ywcheong.commerce.seller.api

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver
import ywcheong.commerce.seller.domain.SellerId
import ywcheong.commerce.seller.domain.SellerRepository

class CurrentSellerArgumentResolver(
    private val sellerRepository: SellerRepository,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(CurrentSeller::class.java) && parameter.parameterType == SellerId::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): SellerId {
        val identityId = CurrentIdentityArgumentResolver.resolveIdentityId(webRequest)
        val seller =
            sellerRepository.findByIdentityId(identityId)
                ?: throw ErrorResponseException(
                    HttpStatus.FORBIDDEN,
                    ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "판매자 등록이 필요합니다."),
                    null,
                )
        return seller.id
    }
}
