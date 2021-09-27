package io.itrunner.heroes.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class HeroDto(
    @Schema(name = "name", example = "Jason", required = true)
    @field:[NotBlank Size(min = 3, max = 30)]
    val name: String = "",

    val createdBy: String?,

    val createdDate: LocalDateTime?,

    val id: Long? = null
)

data class AuthenticationRequest(
    @Schema(name = "username", example = "admin", required = true)
    @field: NotBlank
    val username: String = "",

    @Schema(name = "password", example = "admin", required = true)
    @field: NotBlank
    val password: String = ""
)

data class AuthenticationResponse(val token: String)