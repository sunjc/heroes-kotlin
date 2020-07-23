package io.itrunner.heroes.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("springfox.documentation.swagger")
data class SwaggerProperties(
    val title: String,
    val description: String,
    val version: String,
    val basePackage: String,
    val apiPath: String,
    val contact: Contact
) {
    data class Contact(val name: String, val url: String, val email: String)
}

@ConstructorBinding
@ConfigurationProperties("security")
data class SecurityProperties(val ignorePaths: List<String>, val authPath: String, val cors: Cors, val jwt: Jwt) {
    data class Cors(
        val allowedOrigins: List<String>,
        val allowedMethods: List<String>,
        val allowedHeaders: List<String>
    )

    data class Jwt(val header: String, val secret: String, val expiration: Long, val issuer: String)
}