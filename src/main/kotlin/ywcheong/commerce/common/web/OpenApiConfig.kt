package ywcheong.commerce.common.web

import io.swagger.v3.core.converter.ModelConverter
import io.swagger.v3.core.util.Json
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.UUIDSchema
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OperationCustomizer
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ywcheong.commerce.common.id.EntityId
import ywcheong.commerce.common.passport.CurrentIdentityArgumentResolver.Companion.IDENTITY_HEADER
import ywcheong.commerce.common.passport.Passport

@Configuration
class OpenApiConfig {
    init {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(Passport::class.java)
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
    fun entityIdModelConverter(): ModelConverter =
        ModelConverter { type, context, chain ->
            if (EntityId::class.java.isAssignableFrom(Json.mapper().constructType(type.type).rawClass)) {
                UUIDSchema()
            } else if (chain.hasNext()) {
                chain.next().resolve(type, context, chain)
            } else {
                null
            }
        }

    @Bean
    fun identitySecurityCustomizer(): OperationCustomizer =
        OperationCustomizer { operation, handlerMethod ->
            val requiresIdentity =
                handlerMethod.methodParameters.any { parameter ->
                    parameter.parameterAnnotations.any { it.annotationClass.java.isAnnotationPresent(Passport::class.java) }
                }
            if (requiresIdentity) operation.addSecurityItem(SecurityRequirement().addList(IDENTITY_HEADER))
            operation
        }
}
