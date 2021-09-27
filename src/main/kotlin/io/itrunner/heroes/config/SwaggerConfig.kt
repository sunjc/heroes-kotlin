package io.itrunner.heroes.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(private val info: SwaggerInfoProperties) {
    private val scheme = "bearer"
    private val securitySchemeName = "bearerAuth"
    private val bearerFormat = "JWT"

    @Bean
    fun customOpenAPI(): OpenAPI =
        OpenAPI().addSecurityItem(securityRequirement()).components(securityComponents()).info(info())

    private fun securityRequirement(): SecurityRequirement = SecurityRequirement().addList(securitySchemeName)

    private fun securityComponents(): Components = Components().addSecuritySchemes(
        securitySchemeName,
        SecurityScheme().type(SecurityScheme.Type.HTTP).scheme(scheme).bearerFormat(bearerFormat)
    )

    private fun info(): Info = Info()
        .title(info.title)
        .description(info.description)
        .termsOfService(info.termsOfService)
        .contact(Contact().name(info.contact.name).url(info.contact.url).email(info.contact.email))
        .version(info.version)
}