package ywcheong.commerce.common.web

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ywcheong.commerce.common.passport.CurrentIdentity
import ywcheong.commerce.common.passport.PassportFilter
import ywcheong.commerce.identity.domain.IdentityId
import java.util.UUID

@Configuration
class OpenApiConfig {
    init {
        SpringDocUtils
            .getConfig()
            .addAnnotationsToIgnore(CurrentIdentity::class.java)
            .replaceWithClass(IdentityId::class.java, UUID::class.java)
    }

    @Bean
    fun openApi(): OpenAPI =
        OpenAPI().components(
            Components().addSecuritySchemes(
                PassportFilter.IDENTITY_HEADER,
                SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .`in`(SecurityScheme.In.HEADER)
                    .name(PassportFilter.IDENTITY_HEADER),
            ),
        )

    @Bean
    fun identityIdSecurityCustomizer(): OpenApiCustomizer =
        OpenApiCustomizer { openApi ->
            openApi.paths
                .filterKeys { it.startsWith("/api/") }
                .values
                .flatMap { it.readOperations() }
                .forEach { it.addSecurityItem(SecurityRequirement().addList(PassportFilter.IDENTITY_HEADER)) }
        }
}
