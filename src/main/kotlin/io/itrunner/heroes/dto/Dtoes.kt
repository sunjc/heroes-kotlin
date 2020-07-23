package io.itrunner.heroes.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class HeroDto(
    @ApiModelProperty(value = "name", example = "Jason", required = true)
    @field:[NotBlank Size(min = 3, max = 30)]
    val name: String = "",

    val id: Long? = null
)

data class AuthenticationRequest(
    @ApiModelProperty(value = "username", example = "admin", required = true)
    @field: NotBlank
    val username: String = "",

    @ApiModelProperty(value = "password", example = "admin", required = true)
    @field: NotBlank
    val password: String = ""
)

data class AuthenticationResponse(val token: String)