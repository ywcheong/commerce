package ywcheong.commerce.common.web

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ywcheong.commerce.buyer.api.CurrentBuyerArgumentResolver
import ywcheong.commerce.buyer.usecase.GetBuyer
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver
import ywcheong.commerce.seller.api.CurrentSellerArgumentResolver
import ywcheong.commerce.seller.usecase.GetSeller

@Configuration
class WebConfig(
    private val getBuyer: GetBuyer,
    private val getSeller: GetSeller,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CurrentIdentityArgumentResolver())
        resolvers.add(CurrentBuyerArgumentResolver(getBuyer))
        resolvers.add(CurrentSellerArgumentResolver(getSeller))
    }
}
