package ywcheong.commerce.common.web

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ywcheong.commerce.common.passport.CurrentUser
import ywcheong.commerce.common.passport.PassportFilter
import ywcheong.commerce.user.domain.UserId
import java.util.UUID

@Configuration
class OpenApiConfig {
    init {
        SpringDocUtils
            .getConfig()
            .addAnnotationsToIgnore(CurrentUser::class.java)
            .replaceWithClass(UserId::class.java, UUID::class.java)
    }

    @Bean
    fun openApi(): OpenAPI =
        OpenAPI().components(
            Components().addSecuritySchemes(
                PassportFilter.USER_ID_HEADER,
                SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .`in`(SecurityScheme.In.HEADER)
                    .name(PassportFilter.USER_ID_HEADER),
            ),
        )

    @Bean
    fun userIdSecurityCustomizer(): OpenApiCustomizer =
        OpenApiCustomizer { openApi ->
            openApi.paths
                .filterKeys { it.startsWith("/api/") }
                .values
                .flatMap { it.readOperations() }
                .forEach { it.addSecurityItem(SecurityRequirement().addList(PassportFilter.USER_ID_HEADER)) }
        }
}
