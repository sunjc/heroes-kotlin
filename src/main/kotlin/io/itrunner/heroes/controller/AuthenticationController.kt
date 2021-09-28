package io.itrunner.heroes.controller

import io.itrunner.heroes.dto.AuthenticationRequest
import io.itrunner.heroes.dto.AuthenticationResponse
import io.itrunner.heroes.service.JwtService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/auth"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "Authentication Controller")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService
) {
//    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun login(@RequestBody @Valid request: AuthenticationRequest): AuthenticationResponse {
        // Perform the security
        val authentication: Authentication =
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.username, request.password))
        SecurityContextHolder.getContext().authentication = authentication

        // Generate token
        val token = jwtService.generate(authentication.principal as UserDetails)

        // Return the token
        return AuthenticationResponse(token)
    }
}
