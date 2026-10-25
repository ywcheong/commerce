package ywcheong.commerce.common.web

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ywcheong.commerce.buyer.api.CurrentBuyerArgumentResolver
import ywcheong.commerce.buyer.domain.BuyerRepository
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver
import ywcheong.commerce.seller.api.CurrentSellerArgumentResolver
import ywcheong.commerce.seller.domain.SellerRepository

@Configuration
class WebConfig(
    private val buyerRepository: BuyerRepository,
    private val sellerRepository: SellerRepository,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CurrentIdentityArgumentResolver())
        resolvers.add(CurrentBuyerArgumentResolver(buyerRepository))
        resolvers.add(CurrentSellerArgumentResolver(sellerRepository))
    }
}
