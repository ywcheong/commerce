package ywcheong.commerce.common.web

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OperationCustomizer
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ywcheong.commerce.common.passport.CurrentIdentity
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver.Companion.IDENTITY_HEADER
import ywcheong.commerce.identity.domain.IdentityId
import java.util.UUID

@Configuration
class OpenApiConfig {
    private val passportAnnotations = listOf(CurrentIdentity::class.java)

    init {
        SpringDocUtils
            .getConfig()
            .addAnnotationsToIgnore(*passportAnnotations.toTypedArray())
            .replaceWithClass(IdentityId::class.java, UUID::class.java)
    }

    @Bean
    fun openApi(): OpenAPI =
        OpenAPI().components(
            Components().addSecuritySchemes(
                IDENTITY_HEADER,
                SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .`in`(SecurityScheme.In.HEADER)
                    .name(IDENTITY_HEADER),
            ),
        )

    @Bean
    fun identitySecurityCustomizer(): OperationCustomizer =
        OperationCustomizer { operation, handlerMethod ->
            val requiresIdentity =
                handlerMethod.methodParameters.any { parameter ->
                    passportAnnotations.any { parameter.hasParameterAnnotation(it) }
                }
            if (requiresIdentity) operation.addSecurityItem(SecurityRequirement().addList(IDENTITY_HEADER))
            operation
        }
}
