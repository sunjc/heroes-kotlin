package io.itrunner.heroes.controller

import io.itrunner.heroes.dto.AuthenticationRequest
import io.itrunner.heroes.dto.AuthenticationResponse
import io.itrunner.heroes.service.JwtService
import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/auth"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Api(tags = ["Authentication Controller"])
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService
) {
    private val log = LoggerFactory.getLogger(javaClass)

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

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationException(e: AuthenticationException) {
        log.error(e.message, e)
    }
}
