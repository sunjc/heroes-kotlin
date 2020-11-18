package io.itrunner.heroes.config

import com.fasterxml.classmate.TypeResolver
import io.itrunner.heroes.exception.ErrorMessage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.oas.annotations.EnableOpenApi
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import java.time.LocalDate

@EnableOpenApi
@Configuration
class SwaggerConfig(private val properties: SwaggerProperties) {

    @Bean
    fun petApi(): Docket {
        return Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.basePackage(properties.basePackage))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .pathMapping("/")
            .directModelSubstitute(LocalDate::class.java, String::class.java)
            .genericModelSubstitutes(ResponseEntity::class.java)
            .additionalModels(TypeResolver().resolve(ErrorMessage::class.java))
            .securitySchemes(listOf(authenticationScheme()))
            .securityContexts(listOf(securityContext()))
            .enableUrlTemplating(false)
    }

    private fun apiInfo(): ApiInfo = ApiInfoBuilder()
        .title(properties.title)
        .description(properties.description)
        .contact(Contact(properties.contact.name, properties.contact.url, properties.contact.email))
        .version(properties.version)
        .build()

    private fun authenticationScheme(): HttpAuthenticationScheme =
        HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("BearerToken").build()

    private fun securityContext(): SecurityContext = SecurityContext.builder()
        .securityReferences(defaultAuth())
        .operationSelector { context -> context.requestMappingPattern().startsWith(properties.apiPath) }
        .build()

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScopes = arrayOf(AuthorizationScope("global", "accessEverything"))
        return listOf(SecurityReference("BearerToken", authorizationScopes))
    }
}