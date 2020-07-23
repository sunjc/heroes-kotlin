package io.itrunner.heroes.config

import io.itrunner.heroes.service.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationTokenFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var securityProperties: SecurityProperties

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var authToken = request.getHeader(securityProperties.jwt.header)

        if (authToken != null && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7)
            try {
                val user = jwtService.verify(authToken)

                if (SecurityContextHolder.getContext().authentication == null) {
                    logger.info("checking authentication for user " + user.username)

                    val authentication = UsernamePasswordAuthenticationToken(user.username, "N/A", user.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: Exception) {
                logger.error(e)
            }
        }

        filterChain.doFilter(request, response)
    }

}