package io.itrunner.heroes.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.itrunner.heroes.config.SecurityProperties
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

const val CLAIM_AUTHORITIES = "authorities"

@Service
class JwtService(private val securityProperties: SecurityProperties) {

    fun generate(user: UserDetails): String {
        val algorithm = Algorithm.HMAC256(securityProperties.jwt.secret)
        return JWT.create()
            .withIssuer(securityProperties.jwt.issuer)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + securityProperties.jwt.expiration * 1000))
            .withSubject(user.username)
            .withArrayClaim(CLAIM_AUTHORITIES, user.authorities.map { it.authority }.toTypedArray())
            .sign(algorithm)
    }

    fun verify(token: String): UserDetails {
        val algorithm = Algorithm.HMAC256(securityProperties.jwt.secret)
        val verifier = JWT.require(algorithm).withIssuer(securityProperties.jwt.issuer).build()
        val jwt = verifier.verify(token)
        return User(jwt.subject,
            "N/A",
            jwt.getClaim(CLAIM_AUTHORITIES).asList(String::class.java).map { SimpleGrantedAuthority(it) })
    }
}