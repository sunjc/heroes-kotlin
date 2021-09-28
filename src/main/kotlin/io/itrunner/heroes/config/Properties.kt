package io.itrunner.heroes.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("springdoc.info")
data class SwaggerInfoProperties(
    val title: String,
    val description: String,
    val termsOfService: String,
    val contact: Contact,
    val version: String
) {
    data class Contact(val name: String, val url: String, val email: String)
}

@ConstructorBinding
@ConfigurationProperties("security")
data class SecurityProperties(val ignorePaths: List<String>, val cors: Cors, val jwt: Jwt) {
    data class Cors(
        val allowedOrigins: List<String>,
        val allowedMethods: List<String>,
        val allowedHeaders: List<String>
    )

    data class Jwt(val secret: String, val expiration: Long, val issuer: String)
}